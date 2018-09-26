extern crate hyper;

use std::io::Read;
use hyper::Client;

fn example_post() {
    let client = Client::new();
    let url = "http://localhost:8080/transactions/getTransaction/340c315d-39ad-4a02-a294-84a74c1c7ddc";
    let response = client.post(url).body("").send();
    match response {
        Ok(response) => println!("Response: {}", response.status),
        Err(_) => println!("Err")
    }
    //let mut buffer = String::new();
}

fn example_get() {
    let client = Client::new();
    let url = "http://localhost:8080/transactions/getTransaction/340c315d-39ad-4a02-a294-84a74c1c7ddc";
    let mut response = match client.get(url).send() {
        Ok(response) => response,
        Err(_) => panic!("not cool"),
    };
    let mut buffer = String::new();
    match response.read_to_string(&mut buffer) {
        Ok(_) => (),
        Err(_) => panic!("I give up."),
    };
    println!("buffer: {}", buffer);
}

fn main() {
  let url = "http://localhost:8080/transactions/getTransaction/340c315d-39ad-4a02-a294-84a74c1c7ddc";

  example_post();
  example_get();
}
