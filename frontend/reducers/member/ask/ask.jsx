export default function (state = [], action){
    switch (action.type) {
        case 'RECEIVE_ASK_LIST':
            return action.list

        default:
            return state
    }
}
