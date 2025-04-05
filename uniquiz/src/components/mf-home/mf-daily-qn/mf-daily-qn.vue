<template>
    <view class="content">
        <uni-section title="每日一题" type="line">
            <uni-card title="每日一题" :extra="qn?.sub" thumbnail="/static/logo.png" @click="to_ans_qs">
                <text class="uni-body">
                    问题({{ qn?.type }})：{{ qn?.content }}<br>
                    <text v-if="qn?.options" v-for="(v, k) in qn?.options" :key="k">
                        {{ k }}: {{ v }}
                        <br>
                    </text>
                    难度：{{ qn?.diff }}
                </text>
            </uni-card>
        </uni-section>
    </view>
</template>

<script lang="ts" setup>
import { useQnStore } from '@/stores/qn'
import { storeToRefs } from 'pinia'
import { onMounted, ref } from 'vue'

// 跳转到答题页面

const to_ans_qs = () => {
    uni.navigateTo({ url: "/pages/quiz/ans-qs" })
}

// 获取token

onMounted(() => {
    req_daily_qn()
})

const qn_store = useQnStore()

const { qn } = storeToRefs(qn_store)

const { req_daily_qn } = qn_store

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