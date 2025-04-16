import { defineStore } from "pinia"
import { useTokenStore } from "./token"
import type { qn_diff } from "./qn"
import { ref } from "vue"

export const useStatStore = defineStore('stat', () => {
    const retry_rate = ref<retry_rate[]>()
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
        })
    }

    return { retry_rate, get_top_10_retry_rate_stat }
})


interface retry_rate {
    id: number
    content: string
    sub: string
    diff: qn_diff
    retry_rate: number
    avg_acc: number
}