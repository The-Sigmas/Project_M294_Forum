import { useState, useEffect } from 'react';

export default function CommentThread({ threadId }) {
  const [comments, setComments] = useState([]); // For storing fetched comments
  const [newComment, setNewComment] = useState(''); // For new comment input
  const [responseMessage, setResponseMessage] = useState(''); // For status messages

  // Fetch all threads and find the one with the correct ID
  useEffect(() => {
    fetch('http://localhost:8080/threads/documents')
      .then((res) => res.json())
      .then((threads) => {
        // Find the thread that matches the given threadId
        const thread = threads.find((t) => t.id === threadId);

        if (thread) {
          setComments(thread.content.comments || []); // Set existing comments or empty array
        } else {
          console.error('Thread not found');
        }
      })
      .catch((error) => {
        console.error('Error fetching threads:', error);
      });
  }, [threadId]);

  // Handle adding a new comment
  const handleAddComment = async (e) => {
    e.preventDefault();

    // Fetch all threads again to find the correct one for updating
    try {
      const response = await fetch('http://localhost:8080/threads/documents');
      const threads = await response.json();
      const threadToUpdate = threads.find((t) => t.id === threadId);

      if (!threadToUpdate) {
        setResponseMessage('Thread not found.');
        return;
      }

      // Add the new comment to the top of the existing comments array
      const updatedComments = [newComment, ...(threadToUpdate.content.comments || [])];
      threadToUpdate.content.comments = updatedComments; // Update the comments in the thread

      // Send a PUT request to update the thread document
      const updateResponse = await fetch(`http://localhost:8080/threads/documents/${threadToUpdate.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content: threadToUpdate.content }), // Send the updated content
      });

      if (updateResponse.ok) {
        setComments(updatedComments); // Update the state with new comments
        setNewComment(''); // Clear the input field
        setResponseMessage('Comment added successfully!');
      } else {
        const errorData = await updateResponse.json();
        console.error('Failed to update thread:', errorData);
        setResponseMessage('Failed to add comment.');
      }
    } catch (error) {
      console.error('Error adding comment:', error);
      setResponseMessage('An error occurred.');
    }
  };

  return (
    <div className="space-y-6">
      <h3 className="text-xl font-semibold">Comments</h3>

      {/* Form to add a new comment */}
      <form onSubmit={handleAddComment} className="space-y-4">
        <textarea
          value={newComment}
          onChange={(e) => setNewComment(e.target.value)}
          placeholder="Write your comment..."
          required
          className="block w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-red-500"
        />
        <button
          type="submit"
          className="px-4 py-2 bg-red-600 text-white rounded-lg shadow hover:bg-red-700"
        >
          Add Comment
        </button>
      </form>

      {/* Display response message if available */}
      {responseMessage && <p>{responseMessage}</p>}

      {/* Display all comments */}
      <div className="space-y-4">
        {comments.length > 0 ? (
          comments.map((comment, index) => (
            <div key={index} className="border p-3 rounded-lg bg-gray-100">
              <p>{comment}</p>
            </div>
          ))
        ) : (
          <p>No comments yet.</p>
        )}
      </div>
    </div>
  );
}

