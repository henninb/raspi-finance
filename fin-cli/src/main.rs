extern crate hyper;

use std::io::Read;
use hyper::Client;
use std::env;
use std::process;

fn example_post( url: &str ) {
    let client = Client::new();
    let response = client.post(url).body("").send();
    match response {
        Ok(response) => println!("Response: {}", response.status),
        Err(_) => println!("Err")
    }
    //let mut buffer = String::new();
    println!("end example_post\n");
}

fn example_post1( url: &str ) {
    let client = Client::new();
    let mut response = match client.post(url).body("").send() {
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

  //let mut argv = env::args();
  //let arg: String = argv.nth(1).unwrap(); // error 1
  //let n: i32 = arg.parse().unwrap(); // error 2
  //println!("{}", n);

  example_post1(&url);
  example_post(&url);
  example_get(&url);
}
