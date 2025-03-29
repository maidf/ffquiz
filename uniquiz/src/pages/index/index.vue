<template>
    <view class="content">

        <button @click="goto_usr_msg">用户信息</button>
        <button @click="login">重新登录</button>
        <button @click="logout(token)">退出登录</button>
        <button @click="logoff(token)">注销账号</button>
    </view>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { ref } from 'vue'
onMounted(() => {
    get_token()
})

const get_token = () => {
    token.value = uni.getStorageSync(
        'Authorization'
    )

    if (!token.value) {
        uni.redirectTo({ url: "/pages/user/login" })
    }
}
const token = ref()


const login = () => {
    uni.redirectTo({ url: "/pages/user/login" })
}

const goto_usr_msg = () => {
    uni.navigateTo({ url: "/pages/user/usr-msg" })
}

const logout = (token: string) => {
    uni.request({
        url: "/api/usr/logout",
        header: { 'Authorization': token }
    })
    uni.redirectTo({ url: "/pages/user/login" })
}

const logoff = (token: string) => {
    uni.request({
        url: "/api/email/logoff",
        header: { 'Authorization': token }
    }).then(res => {
        if (res.statusCode == 200) {
            uni.navigateTo({ url: "/pages/user/logoff" })
        } else {
            alert(res.data)
        }
    }).catch(err => alert(err))
}

</script>

<style lang="scss" scoped>
.content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    button {
        margin-top: 10px;
    }
}
</style>
