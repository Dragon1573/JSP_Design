/*
 * @author Dragon1573
 * @date 2020/11/9
 */
import 'bootstrap/jquery.min';
import 'bootstrap/md5.min';

/**
 * 使用 SHA 对密码进行加密
 */
function encryptPassword() {
    let userData = $('form');
    let password = userData.find('#password');
    let confirm = userData.find('#confirm');
    let encPass = b64_hmac_md5(password, password);
    password.value = encPass;
    if (confirm !== undefined) {
        confirm.value = encPass;
    }
    userData.submit();
}

