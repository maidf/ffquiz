<template>

    <uni-card v-for="(v, k) in banks" :key="k" :title="v.name" :extra="v.subject" thumbnail="/static/logo.png"
        @click="to_bank_qs(v)">
        <text class="uni-body">
            创建者：{{ v.creator }}<br>
            创建时间：{{ v.create_time }}
        </text>
    </uni-card>

</template>

<script lang="ts" setup>
import { type bank, useBankStore } from '@/stores/bank'
import { storeToRefs } from 'pinia'
import { watch } from 'vue'
import { onMounted, ref } from 'vue'

// 跳转到题目列表界面

const to_bank_qs = (v: bank) => {
    uni.navigateTo({
        url: "/pages/tea/tea-bank?bank=" + encodeURIComponent(JSON.stringify(v))
    })
}

// 获取token

onMounted(() => {
    req_banks()
})

// 获取题库
const store = useBankStore()
const { banks } = storeToRefs(store)
const { req_banks } = store

</script>

<style lang="scss" scoped></style>