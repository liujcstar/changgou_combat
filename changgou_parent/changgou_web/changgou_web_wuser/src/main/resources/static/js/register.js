//获取指定的URL参数值 http://localhost/pages/setmeal_detail.html?id=3&name=jack
function getUrlParam(paraName) {
    var url = document.location.toString();
    //alert(url);
    var arrObj = url.split("?");
    if (arrObj.length > 1) {
        var arrPara = arrObj[1].split("&");
        var arr;
        for (var i = 0; i < arrPara.length; i++) {
            arr = arrPara[i].split("=");
            if (arr != null && arr[0] == paraName) {
                return arr[1];
            }
        }
        return "";
    }
    else {
        return "";
    }
}



/**
 * 手机号校验
 1--以1为开头；
 2--第二位可为3,4,5,7,8,中的任意一位；
 3--最后以0-9的9个整数结尾。
 */
function checkTelephone(telephone) {
    var reg = /^[1][3,4,5,7,8][0-9]{9}$/;
    if (!reg.test(telephone)) {
        return false;
    } else {
        return true;
    }
}



var clock = '';//定时器对象，用于页面30秒倒计时效果
var nums = 30;
var validateCodeButton;

//基于定时器实现30秒倒计时效果
function doLoop() {
    validateCodeButton.backgroundColor="#B7B5B6";
    validateCodeButton.disabled = true;//将按钮置为不可点击
    nums--;
    if (nums > 0) {
        validateCodeButton.value = nums + '秒后重新获取';
    } else {
        clearInterval(clock); //清除js定时器
        validateCodeButton.disabled = false;  //将按钮置为可点击
        validateCodeButton.value = '重新获取验证码';
        nums = 30; //重置时间
    }
}




/*
* 校验 不能为空
* */
function checkNull(name) {
    if (name!=null&&name.trim().length>0) {
        return true;
    } else {
        return false;
    }

}


/*
* 校验验证码
* 格式为4位数字
* */
function checkCode4(code) {
    var reg = /^\d{4}$/;
    if (reg.test(code)) {
        return true;
    } else {
        return false;
    }

}

