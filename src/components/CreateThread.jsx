import { useState } from 'react';

export default function CreateThread() {
  const [title,     setTitle]     = useState('');
  const [content,   setContent]   = useState('');
  const [category,  setCategory]  = useState('General');   // NEW

  const handleSubmit = async (e) => {
    e.preventDefault();

    const payload = {
      content: {
        title,
        content,
        category,            
        votes: 0,            
        comments: []
      }
    };

    try {
      const res = await fetch('http://localhost:8080/api/threads', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      if (res.ok) {
        setTitle('');
        setContent('');
        setCategory('General');
      } else {
        console.error('Failed to create thread');
      }
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="">
      <div className="bg-gray-800 rounded-xl shadow-2xl p-8 max-w-3xl w-full">
        <p className="bg-gradient-to-r from-red-500 via-red-700 to-red-900 bg-clip-text text-transparent text-4xl font-bold mb-6">
          Create a Thread
        </p>

        <form onSubmit={handleSubmit} className="space-y-6">

          {/* Title */}
          <div>
            <label htmlFor="title" className="block text-xl font-semibold text-gray-300">Title:</label>
            <input
              id="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
              className="mt-2 w-full px-4 py-3 border border-gray-700 rounded-lg bg-gray-900 text-gray-200"
            />
          </div>

          {/* Content */}
          <div>
            <label htmlFor="content" className="block text-xl font-semibold text-gray-300">Content:</label>
            <textarea
              id="content"
              value={content}
              onChange={(e) => setContent(e.target.value)}
              required
              className="mt-2 w-full h-52 px-4 py-3 border border-gray-700 rounded-lg bg-gray-900 text-gray-200"
            />
          </div>

          {/* Category */}
          <div>
            <label htmlFor="category" className="block text-xl font-semibold text-gray-300">Category:</label>
            <select
              id="category"
              value={category}
              onChange={(e) => setCategory(e.target.value)}
              className="mt-2 w-full px-4 py-3 border border-gray-700 rounded-lg bg-gray-900 text-gray-200"
            >
              <option>General</option>
              <option>Help</option>
              <option>Sports</option>
              <option>Offtopic</option>
            </select>
          </div>

          {/* Submit */}
          <button
            type="submit"
            className="px-6 py-3 bg-red-600 text-white rounded shadow hover:bg-red-700"
          >
            Submit
          </button>
        </form>
      </div>
    </div>
  );
}
