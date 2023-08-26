import axios from "axios";

export async function getProblemInfo(problemid) {
    try {
        const response = await axios.get(`http://localhost:8088/problem/query/probleminfo/${problemid}`);
        return response.data;
    } catch (error) {
        return error;
    }
}