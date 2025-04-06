import { defineStore } from "pinia"
import { ref } from "vue"
import type { qn, qn_rep } from "./qn"
import { useTokenStore } from "./token"


export const useBankStore = defineStore('bank', () => {
    const banks = ref<bank[]>()
    const bank = ref<bank>()
    const qs = ref<qn[]>()

    const req_qs = (bank_id: number) => {
        const { get_token } = useTokenStore()
        const token = get_token()
        uni.request({
            url: "/api/qn/bank/" + bank_id + "/qs",
            header: { 'Authorization': token }
        }).then((rep: any) => {
            if (rep.statusCode == 200) {
                const qn_rep: qn_rep[] = rep.data
                qs.value = qn_rep.map(e => ({
                    ...e,
                    options: e.options ? JSON.parse(e.options) : {}
                }))
            } else {

                uni.showToast({ title: rep.data.toString() })
            }
        }).catch(err => uni.showToast({ title: err }))
    }

    const req_banks = () => {
        const { get_token } = useTokenStore()
        const token = get_token()
        uni.request({
            url: "/api/bank/all",
            header: { 'Authorization': token }
        }).then((rep: any) => {
            if (rep.statusCode == 200) {
                banks.value = rep.data
            } else {

                uni.showToast({ title: rep.data.toString() })
            }
        }).catch(err => uni.showToast({ title: err }))
    }


    const req_bank = (bank_id: number) => {
        const { get_token } = useTokenStore()
        const token = get_token()
        uni.request({
            url: "/api/bank/" + bank_id,
            header: { 'Authorization': token }
        }).then((rep: any) => {
            if (rep.statusCode == 200) {
                bank.value = rep.data
            } else {

                uni.showToast({ title: rep.data.toString() })
            }
        }).catch(err => uni.showToast({ title: err }))
    }

    const add_bank = (b: bank) => {
        const { get_token } = useTokenStore()
        const token = get_token()
        uni.request({
            url: '/api/bank',
            data: {
                ...b,
                create_time: null
            },
            method: "POST",
            header: { 'Authorization': token }
        }).then((res: any) => {
            uni.showToast({ title: res.data.toString() })
        }).catch(err => uni.showToast({ title: err }))
    }

    const update_bank = (b: bank) => {
        const { get_token } = useTokenStore()
        const token = get_token()
        uni.request({
            url: '/api/bank/' + b.id,
            data: {
                ...b,
                create_time: null
            },
            method: "PUT",
            header: { 'Authorization': token }
        }).then((res: any) => {
            uni.showToast({ title: res.data.toString() })
        }).catch(err => uni.showToast({ title: err }))
    }

    const delete_bank = (id: number) => {
        const { get_token } = useTokenStore()
        const token = get_token()
        uni.request({
            url: '/api/bank/' + id,
            method: "DELETE",
            header: { 'Authorization': token }
        }).then((res: any) => {
            if (res.statusCode == 200) {
                uni.navigateBack()
            }
            uni.showToast({ title: res.data.toString() })
        }).catch(err => uni.showToast({ title: err }))
    }

    return { bank, banks, qs, req_bank, req_banks, add_bank, update_bank, delete_bank, req_qs }
})


export interface bank {
    id: number
    name: string
    subject: string
    creator: string
    create_time: Date
}