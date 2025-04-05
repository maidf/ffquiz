<template>
    <view class="content">
        <uni-section title="随机刷题" type="line">
            <uni-card v-for="(v, k) in banks" :key="k" :title="v.name" :extra="v.subject" thumbnail="/static/logo.png"
                @click="to_ans_qs">
                <text class="uni-body">
                    创建者：{{ v.creator }}<br>
                    创建时间：{{ v.create_time }}
                </text>
            </uni-card>
        </uni-section>
    </view>
</template>

<script lang="ts" setup>
import { useBankStore } from '@/stores/bank'
import { storeToRefs } from 'pinia'
import { onMounted, ref } from 'vue'

// 跳转到答题页面

const to_ans_qs = () => {
    uni.navigateTo({ url: "/pages/quiz/random-qs" })
}

onMounted(() => {
    req_banks()
})



// 获取题库
const store = useBankStore()
const { banks } = storeToRefs(store)
const { req_banks } = store

</script>

<style lang="scss" scoped>
.content {
    width: 100%;
    max-width: 100vw;
    /* Match the maximum width to the screen width */
    margin: 0 auto;
    /* Center the content */
    box-sizing: border-box;
    /* Ensure padding and border are included in the width */
}
</style>