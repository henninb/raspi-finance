//https://stackoverflow.com/questions/38995892/how-to-move-tests-into-a-separate-file-for-binaries-in-rusts-cargo
extern crate finance;
//# include!("src/main.rs")

//use super::*;

#[cfg(test)]
mod tests {
    use super::*;

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
    use super::*;

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
