export function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
export function getNowTime(){
  let now = new Date();
  let year = now.getFullYear();
  let month = now.getMonth() + 1; // getMonth返回的月份从0开始，所以需要+1
  let date = now.getDate();
  let hours = now.getHours();
  let minutes = now.getMinutes();
  let seconds = now.getSeconds();

  // 如果月份、日期、分钟和秒小于10，前面补0
  month = month < 10 ? "0" + month : month;
  date = date < 10 ? "0" + date : date;
  minutes = minutes < 10 ? "0" + minutes : minutes;
  seconds = seconds < 10 ? "0" + seconds : seconds;
  return year + "-" + month + "-" + date + " " + hours + ":" + minutes + ":" + seconds;
}
  

  
