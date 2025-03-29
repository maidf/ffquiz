<template>
    <view class="context">
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

        <view class="idtag">
            <radio-group @change="change_tag">
                <label>
                    <radio value="student" :checked="true" />
                    学生
                </label>
                <label>
                    <radio value="teacher" />教师
                </label>
            </radio-group>
        </view>

        <button size="mini" type="button" @click="register(idtag)">注册</button>
        <button size="mini" type="button" @click="login">登录</button>
        <button size="mini" color="primary" id="get_captcha" @click="get_captcha">获取验证码</button>
        <view v-if="imgsrc" class="captcha">
            <image :src="imgsrc" mode="scaleToFill" style="width: 120px; height: 40px;" />
        </view>
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

const idtag = ref('student')

const change_tag = (t: any) => {
    idtag.value = t.detail.value
    console.log("idtag:", idtag.value)
}

const register = (idtag: string) => {
    uni.request({
        url: "/api/" + idtag + "/register",
        data: user_account.value,
        method: "POST"
    }).then((rep) => {
        if (rep.statusCode == 200) {
            uni.setStorage({ key: "Authorization", data: rep.data })
                .then(() => {
                    uni.redirectTo({
                        url: "/pages/user/login"
                    })
                })
            alert(rep.data)
        } else {
            alert(rep.data)
        }

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

const login = () =>{
    uni.navigateTo({url: "/pages/user/login"})
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

.captcha {
    border: 1px blue solid;
    border-radius: 2px;
    margin-top: 5px;
}

.idtag {
    margin-top: 5px;
    margin-bottom: 5px;
}
</style>