<template>
    <mf-tea-banks></mf-tea-banks>
    <mf-usr-fn></mf-usr-fn>
</template>

<script lang="ts" setup>
uni.setTabBarItem({
    index: 0,
    visible: false
})
uni.setTabBarItem({
    index: 1,
    visible: false
})
uni.setTabBarItem({
    index: 2,
    visible: true
})

uni.setTabBarItem({
    index: 3,
    visible: true
})
</script>

<style lang="scss" scoped></style>