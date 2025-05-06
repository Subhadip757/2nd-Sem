import { useState } from "react";
import "./App.css";

function App() {
    const [tasks, setTask] = useState([]);
    const [input, setInput] = useState("");

    const addTask = () => {
        if (input.trim() != "") {
            setTask([...tasks, input]);
            setInput("");
        }
    };

    const deleteTask = () => {
        if (tasks.length > 0) {
            const updatedTasks = tasks.slice(0, -1);
            setTask(updatedTasks);
        }
    };

    return (
        <>
            <header>Task Manger Website</header>
            <div className="main">
                <div>
                    <input
                        type="text"
                        value={input}
                        onChange={(e) => setInput(e.target.value)}
                    />
                    <button onClick={addTask}>Add Task</button>
                    <button onClick={deleteTask}>Delete Task</button>
                    <ul>
                        {tasks.map((task, idx) => (
                            <li key={idx}>{task}</li>
                        ))}
                    </ul>
                </div>
            </div>
        </>
    );
}

export default App;
