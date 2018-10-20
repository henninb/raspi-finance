//requires nightly build
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
extern crate regex;
extern crate pancurses;
extern crate finance;

//use pancurses::{initscr, endwin};
use regex::Regex;
use std::process;
use std::env;
use std::fs::File;
use std::io::{self, Write, BufRead, BufReader};
use hyper::Client;
use hyper::rt::{self, Future, Stream};
use hyper::header::HeaderValue;
use hyper::{Request, Body};
use http::header::CONTENT_LENGTH;
use uuid::Uuid;
use chrono::prelude::*;
use chrono::{DateTime};
//use std::time::{SystemTime, UNIX_EPOCH};
//use std::path::Path;

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

//fn fetch_user_input() -> Result<String, io::Error> {
//  let mut user_input = String::new();
//  let stdin = io::stdin();
//  let mut handle = stdin.lock();
//  
//    print!("Enter the data: ");
//    io::stdout().flush().unwrap();
//    handle.read_line(&mut user_input)?;
//    let len = user_input.len();
//    //TODO: windows CRLF at the end of the string
//    if user_input.contains("\r\n") {
//        user_input.truncate(len - 2);
//    }
//    if user_input.contains("\n") {
//        user_input.truncate(len - 1);
//    }
//    Ok(user_input)
//}


/*
fn compute_date_doy( year: i32, month: u32, day: u32 ) -> u32 {
    let n1 = 275 * month / 9;
    let n2 = (month + 9) / 12;
    let n3 = 1 + ((year - 4 * (year / 4) + 2) / 3);
    let n = n1 - (n2 * n3 as u32) + day - 30;

    return n;
}

fn datetime_to_epoch( utc: DateTime<Utc> ) -> u32 {
    let mut idx = 1970;
    let mut total_days = 0;

    while  idx < utc.year()  {
      total_days = total_days + compute_date_doy(idx, 12, 31);
      idx = idx + 1;
    }
    total_days = total_days + compute_date_doy(utc.year(), utc.month(), utc.day() - 1);
    let total_secs = (total_days * 86400) + (utc.hour() * 60 * 60) + (utc.minute() * 60) + utc.second();
    return total_secs;
}
*/

/*
pub fn date_string_to_date( date_string: &str ) -> DateTime<Utc> {
    let re1 = Regex::new(r"^(?P<month>\d{2})-(?P<day>\d{2})$").unwrap();
    let re2 = Regex::new(r"^(?P<month>\d{2})-(?P<day>\d{1})$").unwrap();
    let re3 = Regex::new(r"^(?P<month>\d{1})-(?P<day>\d{1})$").unwrap();
    let re4 = Regex::new(r"^(?P<month>\d{1})-(?P<day>\d{2})$").unwrap();
    let re5 = Regex::new(r"^(?P<month>\d{2})-(?P<day>\d{2})-(?P<year>\d{4})$").unwrap();
    let re6 = Regex::new(r"^(?P<month>\d{1})-(?P<day>\d{1})-(?P<year>\d{4})$").unwrap();
    let re7 = Regex::new(r"^(?P<month>\d{1})-(?P<day>\d{2})-(?P<year>\d{4})$").unwrap();
    let re8 = Regex::new(r"^(?P<month>\d{2})-(?P<day>\d{1})-(?P<year>\d{4})$").unwrap();
    let re9 = Regex::new(r"^(?P<month>\d{2})-(?P<day>\d{2})-(?P<year>\d{2})$").unwrap();
    let re10 = Regex::new(r"^(?P<month>\d{1})-(?P<day>\d{1})-(?P<year>\d{2})$").unwrap();
    let re11 = Regex::new(r"^(?P<month>\d{1})-(?P<day>\d{2})-(?P<year>\d{2})$").unwrap();
    let re12 = Regex::new(r"^(?P<month>\d{2})-(?P<day>\d{1})-(?P<year>\d{2})$").unwrap();

    let date_string = str::replace(&date_string, "/", "-");
    let date_string = re1.replace_all(&date_string, "$month||$day");
    let date_string = re2.replace_all(&date_string, "$month||0$day");
    let date_string = re3.replace_all(&date_string, "0$month||0$day");
    let date_string = re4.replace_all(&date_string, "0$month||$day");
    let date_string = re5.replace_all(&date_string, "$month||$day||$year");
    let date_string = re6.replace_all(&date_string, "0$month||0$day||$year");
    let date_string = re7.replace_all(&date_string, "0$month||$day||$year");
    let date_string = re8.replace_all(&date_string, "$month||0$day||$year");
    let date_string = re9.replace_all(&date_string, "$month||$day||20$year");
    let date_string = re10.replace_all(&date_string, "0$month||0$day||20$year");
    let date_string = re11.replace_all(&date_string, "0$month||$day||20$year");
    let date_string = re12.replace_all(&date_string, "$month||0$day||20$year");

    let dd: String = date_string.chars().skip(4).take(2).collect();
    let mm: String = date_string.chars().skip(0).take(2).collect();
    let mut yy: String = date_string.chars().skip(8).take(4).collect();
    if date_string.len() == 6 {
        yy = Utc::now().year().to_string();
    }

    let utc_datetime = Utc.ymd(yy.parse::<i32>().unwrap(), mm.parse::<u32>().unwrap(), dd.parse::<u32>().unwrap()).and_hms(0, 0, 0);

    return utc_datetime;
}
*/

fn populate_transaction( transaction_vec:  Vec<&str> ) -> Transaction {
    let date_utc = finance::date_string_to_date(transaction_vec[2]);

    let transaction = Transaction {
        guid: Uuid::new_v4().to_string(),
        sha256: "".to_string(),
        accountType: "credit".to_string(),
        accountNameOwner: transaction_vec[1].to_string(),
        description: transaction_vec[3].to_string(),
        category: transaction_vec[4].to_string(),
        notes: transaction_vec[7].to_string(),
        cleared: transaction_vec[6].to_string(),
        reoccurring: "False".to_string(),
        amount: transaction_vec[5].to_string(),
        transactionDate: finance::datetime_to_epoch(date_utc).to_string(),
        dateUpdated: "0".to_string(),
        dateAdded: "0".to_string()
    };
    transaction
}

fn file_read( ifname: String ) -> String {
    let ifp = File::open(ifname).expect("Unable to open file");
    let ifp = BufReader::new(ifp);

    for line in ifp.lines() {
        let line = line.expect("Unable to read line");
        let server_name = "localhost".to_owned();
        let server_port = "8080".to_owned();

        let elements: Vec<_> = line.split("\t").collect();
        
        if elements.len() == 8 {
            if elements[0] == "n" {
                let transaction = populate_transaction(elements);
                insert_post(server_name, server_port, transaction);
            } else if elements[0] == "u" {
                let transaction = populate_transaction(elements);
                update_post(server_name, server_port, transaction);
            } else if elements[0] == "d" {
            } else {
                println!("bad record");
                println!("Line: {}", line);
            }
        } else {
            println!("element count is not equal to 8");
            println!("Line: {}", line);
        }
    }
    //close file
    "".to_string()
}

fn update_post(server_name: String, server_port: String, transaction: Transaction ) {
    let post_url = format!("http://{}:{}/transactions/updateTransaction", server_name, server_port);
    let post_url = post_url.parse::<hyper::Uri>().unwrap();
    if post_url.scheme_part().map(|s| s.as_ref()) != Some("http") {
        eprintln!("This example only works with http URLs.");
        process::exit(104);
    }

    rt::run(insertTransaction(post_url, transaction));
}

fn insert_post(server_name: String, server_port: String, transaction: Transaction ) {
    let post_url = format!("http://{}:{}/transactions/insertTransaction", server_name, server_port);
    let post_url = post_url.parse::<hyper::Uri>().unwrap();
    if post_url.scheme_part().map(|s| s.as_ref()) != Some("http") {
        eprintln!("This example only works with http URLs.");
        process::exit(103);
    }

    rt::run(insertTransaction(post_url, transaction));
}

fn main() {
    pretty_env_logger::init();

    let args: Vec<String> = env::args().collect();
    //if args.len() != 1 {
    //  eprintln!("Usage: {} <noargs>", args[0]);
    //  process::exit(1);
    //}

    let cmd = match env::args().nth(1) {
        Some(cmd) => cmd,
        None => {
            eprintln!("Usage: {} <cmd>", args[0]);
            process::exit(1);
        }
    };

    let server_name = "localhost".to_owned();
    let server_port = "8080".to_owned();

    println!("*** single char start ***");
    finance::read_single_char();
    println!("*** single char done ***");

    file_read("input.txt".to_string());

    if cmd.to_string() == "insert" {
    } else if cmd.to_string() == "select" {

        let guid = match env::args().nth(2) {
            Some(guid) => guid,
            None => {
                eprintln!("Usage: {} select <guid>", args[0]);
                process::exit(101);
            }
        };
        let get_url = format!("http://{}:{}/transactions/getTransaction/{}", server_name, server_port, guid);
        let get_url = get_url.parse::<hyper::Uri>().unwrap();
        rt::run(selectTransaction(get_url));
    } else {
        eprintln!("options not valid");
        process::exit(102);
    }
}

#[allow(non_snake_case)]
fn selectTransaction(url: hyper::Uri) -> impl Future<Item=(), Error=()> {
    let client = Client::new();

    client
        .get(url)
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
        .map(|_| {
            println!("\n\nDone.");
        })
        .map_err(|err| {
            eprintln!("Error {}", err);
        })
}

#[allow(non_snake_case)]
fn insertTransaction(url: hyper::Uri, transaction:  Transaction) -> impl Future<Item=(), Error=()> {
    let client = Client::new();

    //let mut user_input = "".to_string();
    //match fetch_user_input() {
    //    Ok(s) => user_input = s,
    //    Err(error) => eprintln!("error.")
    //};

    //let window = initscr();
    //window.printw("Hello Rust");
    //window.refresh();
    //window.getch();
    //endwin();

    //handle errors with json serialization
    let json = serde_json::to_string(&transaction).unwrap();
    println!("serialize={}", json);
    let mut new_request = Request::new(Body::from(json.clone()));
    *new_request.method_mut() = http::Method::POST;
    *new_request.uri_mut() = url.clone();
    new_request.headers_mut().insert("content-type", HeaderValue::from_static("application/json"));
    new_request.headers_mut().insert("authorization", HeaderValue::from_static("redacted"));
    let len = json.len();
    new_request.headers_mut().insert(CONTENT_LENGTH, HeaderValue::from_str(len.to_string().as_str()).expect(""));

    client
        .request(new_request)
        .and_then(|new_request| {
            println!("POST: {}", new_request.status());
            new_request.into_body().concat2()
        })
        .map(|_| {
            println!("\n\nrequest.status=");
        })
        .map_err(|err| {
            eprintln!("Error {}", err);
        })
}
