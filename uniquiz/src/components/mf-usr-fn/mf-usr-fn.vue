<template>
    <uni-fab ref="fab" :pattern="pattern" :content="content" horizontal="right" vertical="bottom" direction="vertical"
        @trigger="trigger" />
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useUsrStore } from '@/stores/usr'
import { useTokenStore } from '@/stores/token'

const pattern = ref({
    color: '#7A7E83',
    backgroundColor: '#fff',
    selectedColor: '#007AFF',
    buttonColor: '#007AFF',
    iconColor: '#fff'
})

const content = ref([
    {
        iconPath: '/static/bank.png',
        selectedIconPath: '/static/bank.png',
        text: '题库',
        active: false
    },
    {
        iconPath: '/static/paper.png',
        selectedIconPath: '/static/paper.png',
        text: '试卷',
        active: false
    },
    {
        iconPath: '/static/user.png',
        selectedIconPath: '/static/user.png',
        text: '用户信息',
        active: false
    },
    {
        iconPath: '/static/logout.png',
        selectedIconPath: '/static/logout.png',
        text: '退出登录',
        active: false
    },
    {
        iconPath: '/static/logoff.png',
        selectedIconPath: '/static/logoff.png',
        text: '注销账号',
        active: false
    }
])


const trigger = (e: any) => {
    const tag = get_tag()
    switch (e.item.text) {
        case '题库':
            if (tag == "teacher" && teacher) {
                uni.redirectTo({ url: "/pages/tea/tea-home" })
            } else {
                uni.redirectTo({ url: "/pages/index/home" })
            }
            break
        case '试卷':
            if (tag == "teacher" && teacher) {
                uni.redirectTo({ url: "/pages/tea/tea-home2" })
            }
            break
        case '用户信息':
            uni.navigateTo({ url: "/pages/user/usr-msg" })
            break
        case '退出登录':
            uni.clearStorage()
            logout()
            break
        case '注销账号':
            uni.clearStorage()
            logoff()
            break
    }
}

const { logout, logoff } = useUsrStore()

const { teacher, get_tag } = useTokenStore()

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