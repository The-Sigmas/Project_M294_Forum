import { useState } from 'react';

export default function CreateThread() {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    const payload = {
      content: {
        title: title,
        content: content,
        comments: ["Initial Comment"]
      }
    };

    try {
      const response = await fetch('http://localhost:8080/api/threads', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        console.log('Thread created successfully!');
        window.location.reload();
      } else {
        console.error('Failed to create thread');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <div className="">
      <div className="bg-gray-800 rounded-xl shadow-2xl p-8 max-w-3xl w-full">
        {/* Header with gradient text */}
        <p className="bg-gradient-to-r from-red-500 via-red-700 to-red-900 bg-clip-text text-transparent text-4xl font-bold mb-6">
          Create a Thread
        </p>

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Thread Title */}
          <div>
            <label htmlFor="title" className="block text-xl font-semibold text-gray-300">Thread Title:</label>
            <input
              type="text"
              id="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Enter thread title"
              required
              className="mt-2 block w-full px-4 py-3 border border-gray-400 rounded-lg shadow-lg focus:outline-none focus:ring-red-500 focus:border-red-500 text-lg bg-gray-900 text-gray-200"
            />
          </div>

          {/* Thread Content */}
          <div>
            <label htmlFor="content" className="block text-xl font-semibold text-gray-300">Thread Content:</label>
            <textarea
              id="content"
              value={content}
              onChange={(e) => setContent(e.target.value)}
              placeholder="Write your thread content..."
              required
              className="mt-2 block w-full h-52 px-4 py-3 border border-gray-400 rounded-lg shadow-lg focus:outline-none focus:ring-red-500 focus:border-red-500 text-lg bg-gray-900 text-gray-200"
            />
          </div>

          {/* Submit Button */}
          <button 
            type="submit" 
            className="inline-flex items-center px-6 py-3 border border-transparent text-xl font-semibold rounded-lg text-white bg-red-600 shadow-lg hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
          >
            Submit Thread
          </button>
        </form>
      </div>
    </div>
  );
}
