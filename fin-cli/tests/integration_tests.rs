//https://stackoverflow.com/questions/38995892/how-to-move-tests-into-a-separate-file-for-binaries-in-rusts-cargo
extern crate finance;
extern crate hyper;
extern crate env_logger;

//use hyper::{Body, client::Client, header, rt::Future, rt::Stream, Response};
//use super::*;

/*

fn result_to_bytes(res: Response<Body>) -> String {
    let mut body = res.into_body().wait();
    let items = body.get_mut().concat2().wait().unwrap().to_vec();
    String::from_utf8(items).unwrap()
}

#[test]
fn test_sequential_mock() {
    env_logger::try_init().ok();
    let mut c = SequentialConnector::default();
    c.content.push("HTTP/1.1 200 OK\r\n\
                        Server: BOGUS\r\n\
                        \r\n\
                        1".to_string());

    c.content.push("HTTP/1.1 200 OK\r\n\
                        Server: BOGUS\r\n\
                        \r\n\
                        2".to_string());

    let mut core = Core::new().unwrap();
    let client = Client::builder()
        .build::<SequentialConnector, Body>(c);

    let req = client.get("http://127.0.0.1".parse().unwrap());
    let res = core.run(req).unwrap();
    assert_eq!(result_to_bytes(res), "1");

    let req = client.get("http://127.0.0.1".parse().unwrap());
    let res = core.run(req).unwrap();
    assert_eq!(result_to_bytes(res), "2");
}
*/

#[cfg(test)]
mod tests {
    //use super::*;

    #[test]
    fn it_works1() {
        
        assert_eq!(4, 4);
    }
}

#[test]
#[should_panic]
fn test_invalid_matrices_multiplication() {
  panic!("Error message.");
  //let x = 1/0;
}

mod mymodule {
    //use super::*;

    #[test]
    fn it_works1() {
        assert_eq!(4, 4);
    }
}

#[test]
fn it_works() {
    //compute_date_doy1(2018, 12, 1);
    assert!(true);
}

#[test]
#[ignore]
fn expensive_test() {
    // code that takes an hour to run
}
