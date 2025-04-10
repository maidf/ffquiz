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


        <button size="mini" type="button" @click="login(idtag)">登录</button>
        <button size="mini" @click="register">注册</button>
        <button size="mini" color="primary" id="get_captcha" @click="get_captcha">获取验证码</button>
        <view v-if="imgsrc" class="captcha">
            <image :src="imgsrc" mode="scaleToFill" style="width: 120px; height: 40px;" />
        </view>
    </view>
</template>

<script lang="ts" setup>
import { useTokenStore } from '@/stores/token'
import { storeToRefs } from 'pinia'
import { ref } from 'vue'

const user_account = ref({
    account: '',
    password: '',
    captcha: ''
})

const imgsrc = ref()


const change_tag = (t: any) => {
    idtag.value = t.detail.value
}
const idtag = ref('student')

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

const login = (idtag: string) => {
    uni.request({
        url: "/api/" + idtag + "/login",
        data: user_account.value,
        method: "POST"
    }).then((rep) => {
        if (rep.statusCode == 200) {
            // 保存状态
            const token = rep.data.toString()
            const store = useTokenStore()
            const { save } = store
            save(token)
            uni.setStorage({ key: "Authorization", data: rep.data })
                .then(() => {
                    const { teacher } = storeToRefs(store)
                    uni.setStorageSync("login_tag", idtag)
                    if (idtag == 'teacher' && teacher.value) {
                        uni.switchTab({
                            url: "/pages/tea/tea-home"
                        })
                    } else {
                        uni.switchTab({
                            url: "/pages/index/home"
                        })
                    }

                })
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
        if (rep.statusCode == 200) {
            const base64 = uni.arrayBufferToBase64(rep.data)
            imgsrc.value = `data:image/png;base64,${base64}`
        } else {
            uni.showToast({ title: "获取失败", icon: 'none' })
        }
    }).catch((err) => {
        uni.showToast({ title: err, icon: 'none' })
    })
}


const register = () => {
    uni.navigateTo({ url: "/pages/user/register" })
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

.navigator {
    width: 60px;
    height: 25px;
    border: 1px solid black;
    border-radius: 3px;
    text-align: center;
    background-color: blueviolet;
    margin: 5px;
}
</style>