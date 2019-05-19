<template>
    <div>
        <div ref="editor" style="text-align:left"></div>
    </div>
</template>

<script>
    import E from 'wangeditor'

    export default {
        name: 'editor',
        data() {
            return {
                messageTheme: "",
                editor: null
            }
        },
        methods: {
            getContent: function () {
                alert(this.editorContent)
            }
        },
        mounted() {
            var self = this
            self.editor = new E(this.$refs.editor)
            self.editor.customConfig.uploadImgShowBase64 = true
            self.editor.customConfig.onchange = function (html) {

                self.$emit("change", html)
            }
            self.editor.create()
        }
    }
</script>

<style scoped>
</style>