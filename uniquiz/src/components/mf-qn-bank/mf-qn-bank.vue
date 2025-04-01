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
import { onMounted, ref } from 'vue'

// 跳转到答题页面

const to_ans_qs = () => {
    uni.navigateTo({ url: "/pages/quiz/random-qs" })
}

// 获取token

onMounted(() => {
    get_token()
    get_banks()
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




// 获取题库


interface qn_bank {
    id: number
    name: string
    subject: string
    creator: string
    create_time: Date
}

interface bank_res {
    id: number
    name: string
    subject: string
    creator: string
    createTime: Date
}

const banks = ref<[qn_bank]>([{
    id: 0,
    name: '',
    subject: '',
    creator: '',
    create_time: new Date
}])

const to_bank = (bank: bank_res) => {
    return {
        id: bank.id,
        name: bank.name,
        subject: bank.subject,
        creator: bank.creator,
        create_time: bank.createTime
    }
}

const get_banks = () => {
    uni.request({
        url: '/api/question/bank',
        header: { 'Authorization': token.value }
    }).then((res: any) => {
        if (res.statusCode == 200) {
            const data: [bank_res] = res.data
            banks.value.pop()
            data.forEach(d => {
                banks.value.push(to_bank(d))
            })
        } else {
            uni.showToast({ title: res.data.toString() })
        }
    }).catch(err => uni.showToast({ title: err }))
}

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