<template>
    <view class="content">
        <uni-card title="题库信息" thumbnail="/static/logo.png">
            <view class="uni-body">
                <uni-forms :modelValue="bank">
                    <uni-forms-item label="题库名称" name="name">
                        <uni-easyinput type="text" v-model="bank.name" />
                    </uni-forms-item>
                    <uni-forms-item label="题库标签" name="subject">
                        <uni-easyinput type="text" v-model="bank.subject" />
                    </uni-forms-item>
                    <uni-forms-item label="题库作者" name="creator">
                        <uni-easyinput disabled :inputBorder="false" class="creator"
                            :styles="{ color: '#fff', disableColor: '#696969' }" type="text" v-model="bank.creator" />
                    </uni-forms-item>
                </uni-forms>
                <button size="mini" @click="update_bank(bank)">修改</button>
                <button size="mini" @click="delete_bank(bank.id)">删除</button>
                <button size="mini" @click="show_qs">查看题目</button>
                <button size="mini" @click="show_qn">添加题目</button>
            </view>
        </uni-card>

        <mf-add-qn v-if="mf_qn" :bank_id="bank.id"></mf-add-qn>
        <mf-qs v-if="mf_qs" :bank_id="bank.id"></mf-qs>
    </view>
</template>

<script lang="ts" setup>
import type { bank } from '@/stores/bank'
import { useBankStore } from '@/stores/bank'
import { getCurrentInstance, onMounted, ref } from 'vue'
import mfAddQn from './mf-add-qn/mf-add-qn.vue'
import mfQs from './mf-qs/mf-qs.vue'
import { onLoad } from '@dcloudio/uni-app'

const mf_qs = ref(false)

const show_qs = () => {
    mf_qn.value = false
    mf_qs.value = true
}

const mf_qn = ref(false)

const show_qn = () => {
    mf_qs.value = false
    mf_qn.value = true
}

const bank = ref<bank>({
    id: 0,
    name: '',
    subject: '',
    create_time: new Date,
    creator: ''
})

onLoad((opt: any) => {
    bank.value = JSON.parse(decodeURIComponent(opt.bank))
})

const { update_bank, delete_bank } = useBankStore()
</script>

<style lang="scss" scoped>
button {
    margin-right: 10px;
}

.creator {
    opacity: 1;
}
</style>