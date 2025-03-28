<template>
    <view class="context">
        <view>
            <image :src="imgsrc" mode="scaleToFill" style="width: 120px; height: 40px;" />
        </view>
        <view>
            <label for="account">账号</label>
            <input type="text" v-model="user_account.account" id="account" />
        </view>

        <view>
            <label for="password">密码</label>
            <input type="text" v-model="user_account.password" id="password" />
        </view>

        <view>
            <label for="captcha">验证码</label>
            <input type="text" v-model="user_account.captcha" id="captcha" />
        </view>

        <button size="mini" type="button" @click="login">登录</button>
        <button size="mini" color="primary" id="get_captcha" @click="get_captcha">获取验证码</button>
    </view>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
// import { axios } from 'axios'

const user_account = ref({
    account: '',
    password: '',
    captcha: ''
})

const imgsrc = ref()

const login = () => {
    uni.request({
        url: "/api/teacher/login",
        data: user_account.value,
        method: "POST"
    }).then((rep) => {
        console.log(rep.data)
        uni.setStorage({ key: "Authorization", data: rep.data })
            .then(() => {
                uni.redirectTo({
                    url: "/pages/index/index"
                })
            })
    }).catch(err => {
        alert(err)
    })
}

const get_captcha = () => {
    uni.request({
        url: "/api/captcha",
        responseType: "arraybuffer"
    }).then((rep: any) => {
        const base64 = uni.arrayBufferToBase64(rep.data)
        imgsrc.value = `data:image/png;base64,${base64}`
    }).catch((err) => {
        alert(err)
    })
}
</script>

<style lang="scss" scoped>
.context {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

input {
    border: 1px blue solid;
    border-radius: 2px;
    max-width: 500px;
    margin-left: auto;
    margin-right: auto;
    margin-top: 2px;
}

button {
    margin-top: 2px;
    display: inline;
}
</style>