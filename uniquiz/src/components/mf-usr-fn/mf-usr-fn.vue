<template>
    <uni-fab ref="fab" :pattern="pattern" :content="content" horizontal="right" vertical="bottom" direction="vertical"
        @trigger="trigger" />
</template>

<script lang="ts" setup>
import { useUsrStore } from '@/stores/usr'
import { ref } from 'vue'

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
        text: '答题记录',
        active: false
    },
    {
        iconPath: '/static/paper.png',
        selectedIconPath: '/static/paper.png',
        text: '错题记录',
        active: false
    },
    {
        iconPath: '/static/logout.png',
        selectedIconPath: '/static/logout.png',
        text: '退出登录',
        active: false
    }
])


const trigger = (e: any) => {
    switch (e.item.text) {
        case '答题记录':
            uni.navigateTo({ url: "/pages/index/record" })
            break
        case '错题记录':
            uni.redirectTo({ url: "/pages/index/mistake" })
            break
        case '退出登录':
            uni.clearStorage()
            logout()
            break
    }
}

const { logout } = useUsrStore()


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