//requires nightly build
//#![feature(core_intrinsics)]
#[macro_use] extern crate serde_derive;
#[macro_use] extern crate text_io;
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
extern crate reqwest;
extern crate libc;
extern crate kernel32;
//extern crate termios;
extern {
  pub fn getchar() -> libc::c_int;  
}


//libc::STDIN_FILENO defined in the libc

use pancurses::{initscr, endwin, Window, Input, noecho, resize_term};
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
use chrono::{DateTime};
use std::collections::HashMap;
//use termios::{Termios, TCSANOW, ECHO, ICANON, tcsetattr};
//use libc::STDIN_FILENO;
//use std::time::{SystemTime, UNIX_EPOCH};
//use std::path::Path;

//requires nightly build
//use std::intrinsics::type_name;

#[allow(non_snake_case)]
#[derive(Serialize, Deserialize)]
struct Transaction {
  guid: String,
  accountType: String,
  accountNameOwner: String,
  description: String,
  category: String,
  notes: String,
  cleared: i32,
  amount: String,
  transactionDate: u32,
}

#[allow(non_snake_case)]
#[derive(Serialize, Deserialize)]
struct Account {
  accountNameOwner: String,
}

fn populate_transaction( transaction_vec:  Vec<&str> ) -> Transaction {
    let date_utc = finance::date_string_to_date(transaction_vec[2]);

    let transaction = Transaction {
        guid: Uuid::new_v4().to_string(),
        accountType: "credit".to_string(),
        accountNameOwner: transaction_vec[1].to_string(),
        description: transaction_vec[3].to_string(),
        category: transaction_vec[4].to_string(),
        notes: transaction_vec[7].to_string(),
        cleared: transaction_vec[6].parse::<i32>().unwrap(),
        amount: transaction_vec[5].to_string(),
        //transactionDate: finance::datetime_to_epoch(date_utc.unwrap()).to_string(),
        transactionDate: finance::datetime_to_epoch(date_utc.unwrap()),
    };
    transaction
}

fn file_read( ifname: String ) -> String {
    let ifp = File::open(ifname).expect("Unable to open file.");
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
    //let post_url = format!("http://{}:{}/update", server_name, server_port);
    //let post_url = post_url.parse::<hyper::Uri>().unwrap();
    //if post_url.scheme_part().map(|s| s.as_ref()) != Some("http") {
    //    eprintln!("This example only works with http URLs.");
    //    process::exit(104);
    //}
    //
    //rt::run(insertTransaction(post_url, transaction));
}

fn insert_post(server_name: String, server_port: String, transaction: Transaction ) {
    let post_url = format!("http://{}:{}/insert", server_name, server_port);
    let post_url = post_url.parse::<hyper::Uri>().unwrap();
    if post_url.scheme_part().map(|s| s.as_ref()) != Some("http") {
        eprintln!("This example only works with http URLs.");
        process::exit(103);
    }

    rt::run(insertTransaction(post_url, transaction));
}

fn select_window(main_window: &Window) {
  //main_window.printw("select was pressed (select_window)\n");
  selectAccountsNew(main_window, "localhost".to_string(), "8080".to_string());
}

fn main() {
    pretty_env_logger::init();

    let args: Vec<String> = env::args().collect();

    let cmd = match env::args().nth(1) {
        Some(cmd) => cmd,
        None => {
            eprintln!("Usage: {} <cmd>", args[0]);
            process::exit(1);
        }
    };

    //https://stackoverflow.com/questions/10004895/c-reading-from-stdin-as-characters-are-typed
    //println!("press a key");

    //let std_input_handle: u32 = 10;
    //let mut mode: *mut u32;
    //let mut mode: u32 = 0;
    //scanf(" %c", &choice);
    //let ch = unsafe{ getchar() };
    //unsafe {
      //let hConsole = kernel32::GetStdHandle(std_input_handle);
      //kernel32::GetConsoleMode(hConsole, mode);
      //mode &= ~ (ENABLE_LINE_INPUT | ENABLE_ECHO_INPUT);
      //kernel32::SetConsoleMode(hConsole, mode);
      //let hConsole()
    //}

    let mut done = false;
    let window: Window = initscr();
    let mut ch = Input::Character(' ');
    noecho();
    //resize_term(0, 0);

    while !done {
      window.printw("(s) - select transactions by accountNameOwner\n");
      window.printw("(d) - delete transaction by accountNameOwner\n");
      window.printw("(u) - update transaction by accountNameOwner\n");
      window.printw("(i) - insert transaction into accountNameOwner\n");
      window.printw("Please select a command: ");
      window.refresh();

      ch = match window.getch() {
        Some(n) => n,
        None => {
          pancurses::Input::Character(' ')
        },
      };
      window.printw("\n");

      if ch == Input::Character('s') {
        //window.printw("select was pressed\n");
        window.clear();
        select_window(&window)
      } else if ch == Input::Character('\t') {
        window.printw("tab was pressed\n");
        window.clear();
      } else if ch == Input::Character('q') {
        done = true;
        endwin();
      } else {
        window.printw("*** invalid key was pressed.\n");
        window.clear();
      }
    }

    //let word: String = read!();
    //println!("ANSI code: {}", word);

    //loop {
    //    let mut character = [0];
    //    println!("enter: ");
    //    while let Ok(_) = io::stdin().read(&mut character) {
    //        println!("CHAR {:?}", character[0] as char);
    //    }
    //}

    let server_name = "localhost".to_owned();
    let server_port = "8080".to_owned();

    if cmd.to_string() == "insert" {
        let fname = match env::args().nth(3) {
            Some(fname) => fname,
            None => {
                eprintln!("Usage: {} insert file <fname>", args[0]);
                process::exit(101);
            }
        };
        file_read(fname.clone());
        eprintln!("fname = {}", fname);
    } else if cmd.to_string() == "select" {
        let account_name_owner = match env::args().nth(3) {
            Some(account_name_owner) => account_name_owner,
            None => {
                eprintln!("Usage: {} select account <account_name_owner>", args[0]);
                process::exit(101);
            }
        };

        selectAccounts(server_name.clone(), server_port.clone());
        selectTransactionNew(server_name.clone(), server_port.clone(), account_name_owner.clone());

        let get_url_str = format!("http://{}:{}/get_by_account_name_owner/{}", server_name, server_port, account_name_owner);
        let get_url = get_url_str.parse::<hyper::Uri>().unwrap();
        rt::run(selectTransaction(get_url));
    } else if cmd.to_string() == "delete" {
        let guid = match env::args().nth(2) {
            Some(guid) => guid,
            None => {
                eprintln!("Usage: {} delete <guid>", args[0]);
                process::exit(101);
            }
        };
    } else {
        eprintln!("options not valid");
        process::exit(102);
    }
}

#[allow(non_snake_case)]
// Result must be of type reqwest::Result
//turbofish (::<>) on the enum variant
fn selectAccounts(server_name: String, server_port: String) -> reqwest::Result<()> {
    let request_url = format!("http://{}:{}/select_accounts", server_name, server_port);

    let mut response = reqwest::get(&request_url)?;
    let accounts: Vec<Account> = response.json()?;
    println!("vector count = <{}>", accounts.len());
    for account in &accounts {
        println!("accountNameOwner = {}", account.accountNameOwner);
    }
    Ok(())
}

#[allow(non_snake_case)]
// Result must be of type reqwest::Result
//turbofish (::<>) on the enum variant
fn selectAccountsNew(main_window: &Window, server_name: String, server_port: String) -> reqwest::Result<()> {
    let request_url = format!("http://{}:{}/select_accounts", server_name, server_port);
    let idx = 1;
    let mut account_list = HashMap::new();

    let mut response = reqwest::get(&request_url)?;
    let accounts: Vec<Account> = response.json()?;
    //println!("vector count = <{}>", accounts.len());
    for account in &accounts {
        main_window.printw(account.accountNameOwner.clone());
        main_window.printw("\n");
        account_list.insert(account.accountNameOwner.clone(), "1");
    }
    Ok(())
}


#[allow(non_snake_case)]
// Result must be of type reqwest::Result
//turbofish (::<>) on the enum variant
fn selectTransactionNew(server_name: String, server_port: String, account_name_owner: String) -> reqwest::Result<()> {
    let request_url = format!("http://{}:{}/get_by_account_name_owner/{}", server_name, server_port, account_name_owner);

    let mut response = reqwest::get(&request_url)?;
    let transactions: Vec<Transaction> = response.json()?;
    println!("rec count {}", transactions.len());
    for transaction in &transactions {
        println!("{}", transaction.guid);
    }
    Ok(())
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



    let fname = format!("{}.json", &transaction.guid);
    println!("fname={}", fname);
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
            println!("\nrequest.status=");
        })
        .map_err(|err| {
            eprintln!("Error {}", err);
        })
}

//pub unsafe extern "system" fn SetConsoleMode(
//    hConsoleHandle: HANDLE, 
//    dwMode: DWORD
//) -> BOOL
