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
  hours = hours < 10 ? "0" + hours : hours;
  minutes = minutes < 10 ? "0" + minutes : minutes;
  seconds = seconds < 10 ? "0" + seconds : seconds;
  return year + "-" + month + "-" + date + " " + hours + ":" + minutes + ":" + seconds;
}

export function getBeijingTime() {
  // 创建一个Date对象，表示当前的日期和时间
  const now = new Date();

  // 创建一个Intl.DateTimeFormat对象，用于格式化日期和时间
  const formatter = new Intl.DateTimeFormat("zh-CN", {
    timeZone: "Asia/Shanghai", // 设置时区为北京时间
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
  });

  // 使用formatter来格式化当前的日期和时间
  const beijingTime = formatter.format(now);

  return beijingTime;
}
  

  
