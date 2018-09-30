//#![feature(core_intrinsics)]
#[macro_use]
//#![deny(warnings)]
extern crate serde_derive;
extern crate serde;
extern crate serde_json;
extern crate hyper;
extern crate pretty_env_logger;
extern crate http;
extern crate uuid;
extern crate chrono;


use std::env;
use std::io::{self, Write};
use hyper::Client;
use hyper::rt::{self, Future, Stream};
use hyper::header::HeaderValue;
use hyper::{Request, Body};
use http::header::CONTENT_LENGTH;
//use http::StatusCode;
//use http::header;
use uuid::Uuid;
use chrono::prelude::*;
use chrono::{DateTime};
//requires nightly build
//use std::intrinsics::type_name;

#[allow(non_snake_case)]
#[derive(Serialize, Deserialize)]
struct Transaction {
  guid: String,
  sha256: String,
  accountType: String,
  accountNameOwner: String,
  description: String,
  category: String,
  notes: String,
  cleared: String,
  reoccurring: String,
  amount: String,
  transactionDate: String,
  dateUpdated: String,
  dateAdded: String
}

//requires nightly build
//fn test_type<T>(_: T) {
//    println!("{:?}", unsafe { type_name::<T>() });
//}

fn compute_date_doy( utc: Date<Utc> ) -> u32 {
    let n1 = 275 * utc.month() / 9;
    let n2 = (utc.month() + 9) / 12;
    let n3 = 1 + ((utc.year() - 4 * (utc.year() / 4) + 2) / 3);
    let n = n1 - (n2 * n3 as u32) + utc.day() - 30;

    return n;
}

fn datetime_to_epoch( utc: DateTime<Utc> ) -> u32 {
    let mut idx = 1970;
    let mut total_days = 0;
    //let mut total_secs = 0;

    while  idx < utc.year()  {
      total_days = total_days + compute_date_doy(Utc.ymd(idx, 12, 31));
      idx = idx + 1;
    }
    total_days = total_days + compute_date_doy(Utc.ymd(utc.year(), utc.month(), utc.day() - 1));
    let total_secs = (total_days * 86400) + (utc.hour() * 60 * 60) + (utc.minute() * 60) + utc.second();
    return total_secs;
}

fn main() {
    pretty_env_logger::init();

    let cmd = match env::args().nth(1) {
        Some(cmd) => cmd,
        None => {
            println!("Usage: client <cmd>");
            return;
        }
    };

    if cmd.to_string() == "insert" {
        let post_url = "http://localhost:8080/transactions/insertTransaction".to_string();
        let post_url = post_url.parse::<hyper::Uri>().unwrap();
        rt::run(fetch_url_post(post_url));
    } else if cmd.to_string() == "select" {
        let get_url = "http://localhost:8080/transactions/getTransaction/340c315d-39ad-4a02-a294-84a74c1c7ddc".to_string();
        let get_url = get_url.parse::<hyper::Uri>().unwrap();
        rt::run(fetch_url(get_url));
    } else {
    }

    // HTTPS requires picking a TLS implementation, so give a better
    // warning if the user tries to request an 'https' URL.
    //let url = url.parse::<hyper::Uri>().unwrap();
    //if url.scheme_part().map(|s| s.as_ref()) != Some("http") {
    //    println!("This example only works with 'http' URLs.");
    //    return;
    //}

    // Run the runtime with the future trying to fetch and print this URL.
    //
    // Note that in more complicated use cases, the runtime should probably
    // run on its own, and futures should just be spawned into it.
    //rt::run(fetch_url(url));
    //rt::run(fetch_url_post(post_url));
}

fn fetch_url(url: hyper::Uri) -> impl Future<Item=(), Error=()> {
    let client = Client::new();

    client
        // Fetch the url...
        .get(url)
        // And then, if we get a response back...
        .and_then(|result| {
            println!("Response: {}", result.status());
            println!("Headers: {:#?}", result.headers());

            // The body is a stream, and for_each returns a new Future
            // when the stream is finished, and calls the closure on
            // each chunk of the body...
            result.into_body().for_each(|chunk| {
                io::stdout().write_all(&chunk)
                    .map_err(|e| panic!("example expects stdout is open, error={}", e))
            })
        })
        // If all good, just tell the user...
        .map(|_| {
            println!("\n\nDone.");
        })
        // If there was an error, let the user know...
        .map_err(|err| {
            eprintln!("Error {}", err);
        })
}


fn fetch_url_post(url: hyper::Uri) -> impl Future<Item=(), Error=()> {
    let client = Client::new();

    let transaction = Transaction {
        guid: Uuid::new_v4().to_string(),
        sha256: "".to_string(),
        accountType: "credit".to_string(),
        accountNameOwner: "chase_brian".to_string(),
        description: "test".to_string(),
        category: "test".to_string(),
        notes: "from fin-cli".to_string(),
        cleared: "0".to_string(),
        reoccurring: "False".to_string(),
        amount: "0.0".to_string(),
        transactionDate: datetime_to_epoch(Utc::now()).to_string(),
        dateUpdated: datetime_to_epoch(Utc::now()).to_string(),
        dateAdded: datetime_to_epoch(Utc::now()).to_string()
    };

    let json = serde_json::to_string(&transaction).unwrap();
    println!("serialize={}", json);
    //let json = json.to_string();
    let mut new_request = Request::new(Body::from(json.clone()));
    *new_request.method_mut() = http::Method::POST;
    *new_request.uri_mut() = url.clone();
    new_request.headers_mut().insert("content-type", HeaderValue::from_static("application/json"));
    new_request.headers_mut().insert("authorization", HeaderValue::from_static("redacted"));
    let len = json.len();
    new_request.headers_mut().insert(CONTENT_LENGTH, HeaderValue::from_str(len.to_string().as_str()).expect(""));
    //request.headers_mut().insert(ContentLength(json.len()));
    //request.headers_mut().insert(
    //    hyper::header::CONTENT_TYPE,
    //    HeaderValue::from_static("application/json")
    //);

    //println!("POST body: {:?}", new_request.body());
    //new_request.body().concat().and_then(|body| {
    //let payload = deserialize(&body);
    //println!("POST payload: {:?}", payload);
    //});

    client
        .request(new_request)
        .and_then(|new_request| {
            println!("POST: {}", new_request.status());
            new_request.into_body().concat2()
        })
        // If all good, just tell the user...
        .map(|_| {
            //println!("\n\nrequest.status=<{}>", new_request.status());
            println!("\n\nrequest.status=");
        })
        // If there was an error, let the user know...
        .map_err(|err| {
            eprintln!("Error {}", err);
        })
}