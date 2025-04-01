<template>
    <view class="uni-margin-wrap">
        <swiper class="swiper-box" circular indicator-dots="true" @change="change" autoplay="true">
            <swiper-item v-for="(item, index) in info" :key="index">
                <view class="swiper-item" :class="'swiper-item' + index">
                    <text style="color: #fff; font-size: 32px;">{{ item.content }}</text>
                </view>
            </swiper-item>
        </swiper>
    </view>

    <mf-daily-qn></mf-daily-qn>
    <mf-qn-bank></mf-qn-bank>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

const info = ref([
    {
        // colorClass: 'uni-bg-red',
        url: '/pages/user/usr-msg',
        content: '内容 A'
    },
    {
        // colorClass: 'uni-bg-green',
        url: 'https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/shuijiao.jpg',
        content: '内容 B'
    },
    {
        // colorClass: 'uni-bg-blue',
        url: 'https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/shuijiao.jpg',
        content: '内容 C'
    }
])

const curent = ref(0)

const change = (e: any) => {
    curent.value = e.detail.curent
}
</script>

<style scoped lang="scss">
.uni-margin-wrap {
    width: 690rpx;
    width: 100%;
}

.swiper-box {
    height: 300rpx;
}

swiper-item {
    display: block;
    height: 300rpx;
    line-height: 300rpx;
    text-align: center;
}

.swiper-item0 {
    background-color: #cee1fd;
}

.swiper-item1 {
    background-color: #b2cef7;
}

.swiper-item2 {
    background-color: #cee1fd;
}
</style>