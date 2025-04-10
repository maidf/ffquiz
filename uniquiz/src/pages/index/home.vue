<template>
    <view class="content">
        <mf-home></mf-home>
        <mf-usr-fn></mf-usr-fn>
    </view>
</template>

<script setup lang="ts">
uni.setTabBarItem({
    index: 0,
    visible: true
})
uni.setTabBarItem({
    index: 1,
    visible: true
})
uni.setTabBarItem({
    index: 2,
    visible: false
})

uni.setTabBarItem({
    index: 3,
    visible: false
})
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
