export default function (state = {}, action){
    switch (action.type) {
        case 'RECEIVE_STUDENT_LIST':
            return { ...state, list: action.list };

        case 'RECEIVE_STUDENT_DATA':
            return { ...state, data: action.data };

        default:
            return state
    }
}
