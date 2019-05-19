<template>
    <div id="app">
        <el-container>
            <el-header>

            </el-header>
            <el-main>
                <el-row>
                    <el-table :data="messages" @row-dblclick="clickMessageItem">
                        <el-table-column label="标题" prop="theme"></el-table-column>
                        <el-table-column label="用户">
                            <template slot-scope="scope">
                                <span>{{scope.row.user?scope.row.user.username:"匿名"}}</span>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-row>
                <el-row>
                    <el-button @click="leavingMessage">发表留言</el-button>
                </el-row>
            </el-main>
        </el-container>
        <el-dialog :visible.sync="show">
            <template v-if="reply" v-for="msg in replyMessages">
                <el-row :key="msg.id">
                    <el-col span="6">
                        <img src="" alt=""/>
                        <span> ip: {{msg.ipAddress}}</span>
                    </el-col>
                    <el-col span="18">
                        <el-row v-if="msg.theme"> {{msg.theme}}</el-row>
                        <el-row v-text="msg.content"> </el-row>
                    </el-col>
                </el-row>
            </template>

            <el-row>
                <el-col span="6"></el-col>
                <el-col span="18">
                    <el-form>
                        <el-form-item v-if="!reply" label="主题:">
                            <el-input v-model="theme"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-row>
                                <label style="float: left">{{reply?"回复:":"内容:"}}</label>
                            </el-row>
                            <editor @change="changeContent"></editor>
                        </el-form-item>
                        <el-form-item>
                            <el-row>
                                <el-col span="8">
                                    <el-button type="submit" @click="submitMessage">{{reply?"回复":"发表"}}</el-button>
                                </el-col>
                                <el-col v-if="isManage&& !reply" span="8">
                                    <el-button type="submit" @click="deleteMessage">删除</el-button>
                                </el-col>
                                <el-col v-if="isManage &&!reply" span="8">
                                    <el-button type="submit" @click="auditMessage">通过</el-button>
                                </el-col>
                            </el-row>
                        </el-form-item>
                    </el-form>
                </el-col>
            </el-row>

        </el-dialog>
    </div>
</template>

<script>
    import Editor from "./components/editor";
    import {
        axiosMessageList,
        axiosCurrentUserUsername,
        axiosSubmitMessage,
        axiosManageAuditMessage,
        axiosManageDeleteMessage,
        axiosReplyMessage,
        axiosManageReplyMessage,
        axiosIsManage,
        axiosMessageReplyList

    } from "./utils/service.js";

    export default {
        name: "app",
        data() {
            return {
                isManage: false,
                reply: true,
                show: false,
                messages: [],
                replyMessages: [],
                message: "",
                theme: '',
                content: {},
                quoteId: null,
                username: null
            };
        },
        components: {Editor},
        methods: {
            leavingMessage() {
                let self = this

                self.$set(self, 'reply', false)
                self.$set(self, 'show', true)
            },
            clickMessageItem(row, event, column) {
                var self = this
                self.$set(self, 'show', true)
                self.$set(self, 'quoteId', row.id)
                axiosMessageReplyList(self.quoteId).then(data=>{
                    self.$set(self,"replyMessages",data)
                })

            },
            changeContent(data) {
                let self = this
                self.$set(self, "message", data)
            },
            deleteMessage() {
                axiosManageDeleteMessage(this.quoteId)
            },
            auditMessage() {
                axiosManageAuditMessage(this.quoteId)

            },
            submitMessage() {
                var self = this

                if (!self.reply) {
                    // 留言
                    axiosSubmitMessage({
                        theme: self.theme,
                        message: self.message,
                        quoteId: self.quoteId,
                        username: self.username
                    }).then(data => {
                        if (data) {
                            self.$emit(self, "show", false)
                        }
                    })
                } else {
                    // 回复
                    if (this.isManage) {
                        // 管理员
                        axiosManageReplyMessage(self.quoteId, {
                            audit: true,
                            reply: self.message
                        }).then(data => {
                            if (data) {
                                self.$emit(self, "show", false)
                            }
                        })
                    } else {
                        // 普通用户
                        axiosReplyMessage(self.quoteId, {
                            message: self.message,
                            quoteId: self.quoteId,
                            username: self.username
                        }).then(data => {
                            if (data) {
                                self.$emit(self, "show", false)
                            }
                        })

                    }

                }
            },
            refreshMessageReply() {

            }
        },
        created() {
            var self = this
            axiosMessageList().then(data => {
                self.$set(self, "messages", data || [])
            })
            axiosCurrentUserUsername().then(data => {
                    self.$set(self, 'username', data)
                }
            )
            axiosIsManage().then(data=>{
                    self.$set(self,"isManage",data)
            })
        }
    };
</script>

<style>
    #app {
        font-family: "Avenir", Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
        margin-top: 60px;
    }
</style>
