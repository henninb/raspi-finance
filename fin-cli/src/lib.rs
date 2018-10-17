extern crate chrono;
extern crate regex;

use chrono::prelude::*;
use chrono::{DateTime};
use regex::Regex;

#[test]
fn compute_date_doy_test() {
  let x = compute_date_doy(10, 15, 2018);
  let y = compute_date_doy(6, 4, 2018);
  assert_eq!(x, 2442);
  assert_eq!(y, 2108);
}

#[test]
fn date_string_to_date_test() {
  let yy = "2018";
  let mm = "10";
  let dd = "1";
  let utc_datetime = Utc.ymd(yy.parse::<i32>().unwrap(), mm.parse::<u32>().unwrap(), dd.parse::<u32>().unwrap()).and_hms(0, 0, 0);
  let result_datetime = date_string_to_date(&"10/1".to_string());
  
  assert_eq!(result_datetime, utc_datetime);
}

#[test]
#[should_panic]
fn date_string_to_date_panic_test() {
  date_string_to_date(&"2/30".to_string());
}

#[test]
fn datetime_to_epoch_test() {
  let utc_datetime = Utc.ymd(2018, 4, 1).and_hms(0, 0, 0);
  let epoch = datetime_to_epoch(utc_datetime);
  assert_eq!(1522540800, epoch);
}

#[test]
#[should_panic]
fn datetime_to_epoch_panic_test() {
  let utc_datetime = Utc.ymd(1965, 1, 1).and_hms(0, 0, 0);
  let epoch = datetime_to_epoch(utc_datetime);
}

#[test]
fn datetime_to_epoch_large_date_test() {
   let utc_datetime = Utc.ymd(2038, 1, 20).and_hms(0, 0, 0);
   let epoch = datetime_to_epoch(utc_datetime);
   assert_eq!(2147558400, epoch);
}

fn compute_date_doy( year: i32, month: u32, day: u32 ) -> u32 {
    let n1 = 275 * month / 9;
    let n2 = (month + 9) / 12;
    let n3 = 1 + ((year - 4 * (year / 4) + 2) / 3);
    let n = n1 - (n2 * n3 as u32) + day - 30;

    return n;
}

pub fn datetime_to_epoch( utc: DateTime<Utc> ) -> u32 {
    let mut idx = 1970;
    let mut total_days = 0;

    if utc.year() < 1970 {
      panic!("Year cannot be less than 1970");
    }

    while  idx < utc.year()  {
      total_days = total_days + compute_date_doy(idx, 12, 31);
      idx = idx + 1;
    }
    total_days = total_days + compute_date_doy(utc.year(), utc.month(), utc.day() - 1);
    let total_secs = (total_days * 86400) + (utc.hour() * 60 * 60) + (utc.minute() * 60) + utc.second();
    return total_secs;
}

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
    
    //let year1 = yy.parse::<i32>() {
    //    Ok(year1) => year1,
    //    Err(_) => {
    //        eprintln!("invalid digit for the year.");
    //        process::exit(105);
    //    }
    //};
    let year = yy.parse::<i32>().unwrap();
    let month = mm.parse::<u32>().unwrap();
    let day = dd.parse::<u32>().unwrap();

    let utc_datetime = Utc.ymd(year, month, day).and_hms(0, 0, 0);

    return utc_datetime;
}
