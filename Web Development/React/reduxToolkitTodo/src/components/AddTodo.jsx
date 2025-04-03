import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { addTodo } from '../feature/todo/todoSlice';

function AddTodo() {
    const [input, setInput] = useState('');
    const dispatch = useDispatch();

    const addTodoHandler = (e) => {
        e.preventDefault();

        if (!input.trim()) return; // Prevent adding empty todos
        dispatch(addTodo(input.trim()));
        setInput('');
    };

    return (
        <form 
            onSubmit={addTodoHandler} 
            className="flex items-center gap-4 bg-gray-800 p-4 rounded-lg shadow-lg max-w-md mx-auto mt-12"
        >
            <input
                type="text"
                className="w-full bg-gray-700 rounded-md border border-gray-600 focus:border-indigo-400 focus:ring-2 focus:ring-indigo-500 text-white py-2 px-4 outline-none placeholder-gray-400"
                placeholder="Add a new task..."
                value={input}
                onChange={(e) => setInput(e.target.value)}
            />
            <button
                type="submit"
                className="bg-indigo-500 hover:bg-indigo-600 text-white py-2 px-6 rounded-lg transition duration-200"
            >
                Add
            </button>
        </form>
    );
}

export default AddTodo;