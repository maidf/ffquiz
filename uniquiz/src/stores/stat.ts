import { defineStore } from "pinia"
import { useTokenStore } from "./token"
import type { qn_diff } from "./qn"
import { ref } from "vue"

export const useStatStore = defineStore('stat', () => {
    const retry_rate = ref<retry_rate[]>()
    const bank_qn_nums = ref<bank_qn_nums[]>()

    const get_top_10_retry_rate_stat = () => {
        uni.request({
            url: "/api/stat/retry_rate",
            header: { 'Authorization': useTokenStore().get_token() }
        }).then((res) => {
            if (res.statusCode === 200) {
                retry_rate.value = res.data as retry_rate[]
            } else {
                uni.showToast({ title: res.data.toString() })
            }
        }).catch(err => uni.showToast({ title: err }))
    }

    const get_bank_qn_nums = () => {
        uni.request({
            url: "/api/stat/bank_qn_nums",
            header: { 'Authorization': useTokenStore().get_token() }
        }).then((res) => {
            if (res.statusCode === 200) {
                bank_qn_nums.value = res.data as bank_qn_nums[]
            } else {
                uni.showToast({ title: res.data.toString() })
            }
        }).catch(err => uni.showToast({ title: err }))
    }

    return { retry_rate, bank_qn_nums, get_top_10_retry_rate_stat, get_bank_qn_nums }
})


interface retry_rate {
    id: number
    content: string
    sub: string
    diff: qn_diff
    retry_rate: number
    avg_acc: number
}

interface bank_qn_nums {
    id: number
    name: string
    sub: string
    qn_nums: number
    use_nums: number
    err_nums: number
}