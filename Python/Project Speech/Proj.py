import sqlite3
import speech_recognition as sr
import pyttsx3

# Initialize text-to-speech engine
engine = pyttsx3.init()

# Initialize speech recognizer
recognizer = sr.Recognizer()

# Database setup
def create_database():
    conn = sqlite3.connect('todo.db')
    c = conn.cursor()
    c.execute('''CREATE TABLE IF NOT EXISTS todos
                 (id INTEGER PRIMARY KEY AUTOINCREMENT, task TEXT)''')
    conn.commit()
    conn.close()

# Speak function
def speak(text):
    engine.say(text)
    engine.runAndWait()

# Listen function
def listen():
    with sr.Microphone() as source:
        print("Listening...")
        recognizer.adjust_for_ambient_noise(source)
        audio = recognizer.listen(source)
        try:
            command = recognizer.recognize_google(audio)
            print(f"You said: {command}")
            return command.lower()
        except sr.UnknownValueError:
            speak("Sorry, I did not understand that.")
            return None
        except sr.RequestError:
            speak("Sorry, my speech service is down.")
            return None

# Add a todo
def add_todo(task):
    conn = sqlite3.connect('todo.db')
    c = conn.cursor()
    c.execute("INSERT INTO todos (task) VALUES (?)", (task,))
    conn.commit()
    conn.close()
    speak(f"Added: {task}")

# Update a todo
def update_todo(task_id, new_task):
    conn = sqlite3.connect('todo.db')
    c = conn.cursor()
    c.execute("UPDATE todos SET task = ? WHERE id = ?", (new_task, task_id))
    conn.commit()
    conn.close()
    speak(f"Updated task {task_id} to: {new_task}")

# Delete a todo
def delete_todo(task_id):
    conn = sqlite3.connect('todo.db')
    c = conn.cursor()
    c.execute("DELETE FROM todos WHERE id = ?", (task_id,))
    conn.commit()
    conn.close()
    speak(f"Deleted task {task_id}")

# List all todos
def list_todos():
    conn = sqlite3.connect('todo.db')
    c = conn.cursor()
    c.execute("SELECT * FROM todos")
    todos = c.fetchall()
    conn.close()
    if todos:
        speak("Here are your todos:")
        for todo in todos:
            speak(f"Task {todo[0]}: {todo[1]}")
    else:
        speak("You have no todos.")

# Main function
def main():
    create_database()
    speak("Welcome to your Todo App. How can I help you?")
    while True:
        command = listen()
        if command:
            if "add" in command:
                speak("What task would you like to add?")
                task = listen()
                if task:
                    add_todo(task)
            elif "update" in command:
                speak("Which task ID would you like to update?")
                task_id = listen()
                speak("What is the new task?")
                new_task = listen()
                if task_id and new_task:
                    update_todo(int(task_id), new_task)
            elif "delete" in command:
                speak("Which task ID would you like to delete?")
                task_id = listen()
                if task_id:
                    delete_todo(int(task_id))
            elif "list" in command:
                list_todos()
            elif "exit" in command:
                speak("Goodbye!")
                break
            else:
                speak("Sorry, I don't understand that command.")

if __name__ == "__main__":
    main()