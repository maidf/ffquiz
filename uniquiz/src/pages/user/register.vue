<template>
    <view class="context">
        <uni-forms :modelValue="user_account">
            <uni-forms-item label="账号" name="account">
                <uni-easyinput type="text" v-model="user_account.account" />
            </uni-forms-item>
            <uni-forms-item label="密码" name="password">
                <uni-easyinput type="password" v-model="user_account.password" />
            </uni-forms-item>
            <uni-forms-item name="code" label="验证码">
                <uni-easyinput type="text" v-model="user_account.captcha" />
            </uni-forms-item>
            <uni-forms-item label="身份" name="idtag">
                <uni-data-checkbox v-model="idtag" :localdata="tags" mode="default" @change="change_tag" />
            </uni-forms-item>
        </uni-forms>


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
}

const tags = ref([
    {
        text: '学生',
        value: 'student'
    },
    {
        text: '教师',
        value: 'teacher'
    }
])

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
            uni.showToast({ title: rep.data.toString(), icon: 'none' })
        } else {
            uni.showToast({ title: rep.data.toString(), icon: 'none' })
        }

    }).catch(err => {
        uni.showToast({ title: err, icon: 'none' })
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
        uni.showToast({ title: err, icon: 'none' })
    })
}

const login = () => {
    uni.navigateTo({ url: "/pages/user/login" })
}

</script>

<style lang="scss" scoped>
.context {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}


button {
    margin-top: 10px;
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