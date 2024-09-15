import { useState } from 'react';

export default function CreateThread() {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault(); // Prevent page refresh
    console.log('Title:', title);
    console.log('Content:', content);
    
    // Add logic to handle submission, such as sending data to an API or server
  };

  return(<>
    <p className="bg-gradient-to-r from-red-500 via-red-700 to-red-900 bg-clip-text text-transparent">Create a Thread</p>
    <form onSubmit={ handleSubmit }>
      <input 
        type="text"
        id="title"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        placeholder="Enter thread title"
        required
      />
      <textarea
        id="content"
        value={content}
        onChange={(e) => setContent(e.target.value)}
        placeholder="Write your thread content..."
        required
      />
    </form>
  </>)
}
