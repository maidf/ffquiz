<template>
    <view class="content">

        <mf-home></mf-home>

        <uni-fab ref="fab" :pattern="pattern" :content="content" horizontal="right" vertical="bottom"
            direction="vertical" @trigger="trigger" />
    </view>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'


const pattern = ref({
    color: '#7A7E83',
    backgroundColor: '#fff',
    selectedColor: '#007AFF',
    buttonColor: '#007AFF',
    iconColor: '#fff'
})

const content = ref([{
    iconPath: '/static/user.png',
    selectedIconPath: '/static/logo.png',
    text: '用户信息',
    active: false
},
{
    iconPath: '/static/login.png',
    selectedIconPath: '/static/logo.png',
    text: '重新登录',
    active: false
},
{
    iconPath: '/static/logout.png',
    selectedIconPath: '/static/logo.png',
    text: '退出登录',
    active: false
},
{
    iconPath: '/static/logoff.png',
    selectedIconPath: '/static/logo.png',
    text: '注销账号',
    active: false
}
])


const trigger = (e: any) => {
    switch (e.item.text) {
        case '用户信息':
            goto_usr_msg()
            break
        case '重新登录':
            login()
            break
        case '退出登录':
            logout(token.value)
            break
        case '注销账号':
            logoff(token.value)
            break
    }
}


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
            uni.showToast({ title: res.data.toString(), icon: 'none' })
        }
    }).catch(err => uni.showToast({ title: err, icon: 'none' }))
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
