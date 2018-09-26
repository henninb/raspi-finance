extern crate hyper;

use std::io::Read;
use hyper::Client;
use std::env;
use std::process;
use hyper::header::{Headers, ContentType};
use hyper::mime::{Mime, TopLevel, SubLevel};

fn example_post( url: &str ) {
    let mut headers = Headers::new();
    let client = Client::new();
    let json = "{\"guid\":\"83e30475-fcfc-4f30-8395-0bb40e89b568\",\"sha256\":\"b154020e5cd52d086d1650fa01a82b82f823e67cd59b7b346aeeb781c7dafa44\",\"accountType\":\"credit\",\"accountNameOwner\":\"amazon_store_brian\",\"description\":\"Initial Balance\",\"category\":\"\",\"notes\":\"Account Opened\",\"cleared\":1,\"reoccurring\":false,\"amount\":\"0.0\",\"transactionDate\":1353456000,\"dateUpdated\":1487300996,\"dateAdded\":1487300996}";
    let response = client.post(url).body(json).send();
    match response {
        Ok(response) => println!("Response: {}", response.status),
        Err(_) => println!("Err")
    }
    //let mut buffer = String::new();
    println!("end example_post\n");
}

fn example_post1( url: &str ) {
    let mut headers = Headers::new();
headers.set(
    ContentType::json()
);
    let client = Client::new();
    let json = "{\"guid\":\"83e30475-fcfc-4f30-8395-0bb40e89b568\",\"sha256\":\"b154020e5cd52d086d1650fa01a82b82f823e67cd59b7b346aeeb781c7dafa44\",\"accountType\":\"credit\",\"accountNameOwner\":\"amazon_store_brian\",\"description\":\"Initial Balance\",\"category\":\"\",\"notes\":\"Account Opened\",\"cleared\":1,\"reoccurring\":false,\"amount\":\"0.0\",\"transactionDate\":1353456000,\"dateUpdated\":1487300996,\"dateAdded\":1487300996}";
    let mut response = match client.post(url).headers(headers).body(json).send() {
        Ok(response) => response,
        Err(_) => panic!("not cool"),
    };
    let mut buffer = String::new();
    match response.read_to_string(&mut buffer) {
        Ok(_) => (),
        Err(_) => panic!("I give up."),
    };
    println!("buffer: {}", buffer);
    println!("end example_post1\n");
}

fn example_get( url: &str ) {
    let client = Client::new();
    let mut response = match client.get(url).send() {
        Ok(response) => response,
        Err(_) => panic!("not cool"),
    };
    let mut buffer = String::new();
    match response.read_to_string(&mut buffer) {
        Ok(_) => (),
        Err(_) => panic!("I give up."),
    };
    println!("buffer: {}\n", buffer);
    println!("end example_get\n");
}

fn main() {
  let args: Vec<String> = env::args().collect();
  if args.len() != 1 {
    println!("Usage: {} <noargs>", args[0]);
    process::exit(1);
  }

  let url = "http://localhost:8080/transactions/getTransaction/340c315d-39ad-4a02-a294-84a74c1c7ddc".to_string();
  let post_url = "http://localhost:8080/transactions/insertTransaction".to_string();

  //let mut argv = env::args();
  //let arg: String = argv.nth(1).unwrap(); // error 1
  //let n: i32 = arg.parse().unwrap(); // error 2
  //println!("{}", n);

  example_post1(&post_url);
  //example_post(&post_url);
  example_get(&url);
}
