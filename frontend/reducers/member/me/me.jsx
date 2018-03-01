export default function (state = [], action){
    switch (action.type) {
        case 'RECEIVE_ME_DATA':
            return action.list

        default:
            return state
    }
}