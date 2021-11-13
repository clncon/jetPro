import http from '../utils/http'
const root = 'api/tasking'

export function listTasks () {
  return http.get(
    root + '/list')
}

export function task (jobId) {
  return http.get(root + '/task/' + jobId)
}

export function cancel (jobId) {
  return http.get(root + '/cancel/' + jobId)
}

export function restart (jobId) {
  return http.get(root + '/restart/' + jobId)
}

export function suspend (jobId) {
  return http.get(root + '/suspend/' + jobId)
}

export function resume (jobId) {
  return http.get(root + '/resume/' + jobId)
}

export function submit (data) {
  return http.post(root + '/submit', data)
}
