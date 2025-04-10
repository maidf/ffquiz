<template>

    <uni-card v-for="(v, k) in papers" :key="k" :title="v.name" :extra="v.diff" thumbnail="/static/logo.png"
        @click="to_paper_qs(v)">
        <text class="uni-body">
            创建者：{{ v.creator }}<br>
            创建时间：{{ v.create_time }}
        </text>
    </uni-card>

</template>

<script lang="ts" setup>
import { usePaperStore, type paper } from '@/stores/paper'
import { storeToRefs } from 'pinia'
import { onMounted } from 'vue'

// 跳转到题目列表界面

const to_paper_qs = (v: paper) => {
    uni.navigateTo({
        url: "/pages/quiz/exam?paper=" + encodeURIComponent(JSON.stringify(v))
    })
}

// 获取token

onMounted(() => {
    req_papers()
})

// 获取试卷
const store = usePaperStore()

const { papers } = storeToRefs(store)
const { req_papers } = store

</script>

<style lang="scss" scoped></style>