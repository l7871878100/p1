export function axiosMessageList(params={}) {

    let page, size
    if (params) {
        page = params.page
        size = params.size
    }
    return window.axios.get("/messages/theme").then(response => {
        let data = response.data
        return data
    })
}

export function axiosSubmitMessage(messageForm) {
    return window.axios.post("/message/publish", messageForm).then(res=>{
        return res.data
    })
}

export function axiosCurrentUserUsername() {
    return window.axios.get("/current-username").then(res=>{
          return   res.data
        }
    )
}

export function axiosIsManage() {
    return window.axios.get("/isManage").then(res=>{
           return  res.data
        }
    )
}
export function axiosManageDeleteMessage(id) {
    return window.axios.delete("/admin/delete/"+id).then(res=>{
           return  res.data
        }
    )
}
export function axiosManageAuditMessage(id) {
    return window.axios.post("/admin/audit/"+id).then(res=>{
           return  res.data
        }
    )
}
export function axiosManageReplyMessage(id,auditForm) {
    return window.axios.post("/admin/reply/"+id,auditForm).then(res=>{
           return  res.data
        }
    )
}

export function axiosReplyMessage(id,auditForm) {
    return window.axios.post("/message/"+id+"/reply",auditForm).then(res=>{
           return  res.data
        }
    )
}

export function axiosMessageReplyList(id){
    return window.axios.get("/messages/"+id+"/replyList").then(res=>res.data)
}