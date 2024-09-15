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
    <form onSubmit={handleSubmit} className="space-y-4">
        {/* Thread Title */}
        <div>
          <label htmlFor="title" className="block text-sm font-medium text-gray-700">Thread Title:</label>
          <input
            type="text"
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Enter thread title"
            required
            className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
          />
        </div>
        
        {/* Thread Content */}
        <div>
          <label htmlFor="content" className="block text-sm font-medium text-gray-700">Thread Content:</label>
          <textarea
            id="content"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            placeholder="Write your thread content..."
            required
            className="mt-1 block w-full h-40 px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
          />
        </div>
        
        {/* Submit Button */}
        <button 
          type="submit" 
          className="inline-flex items-center px-4 py-2 border border-transparent text-base font-medium rounded-md text-white bg-indigo-600 shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        >
          Submit Thread
        </button>
      </form>
  </>)
}
