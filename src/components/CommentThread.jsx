import { useState, useEffect } from 'react';

export default function CommentThread({ threadId }) {
  const [comments, setComments] = useState([]); // For storing fetched comments
  const [newComment, setNewComment] = useState(''); // For new comment input
  const [responseMessage, setResponseMessage] = useState(''); // For status messages

  // Fetch the existing comments when the component mounts or threadId changes
  useEffect(() => {
    fetch(`http://localhost:8080/threads/documents/${threadId}`)
      .then((res) => res.json())
      .then((data) => {
        setComments(data.content.comments); // Assuming the comments are in content.comments
      })
      .catch((error) => {
        console.error('Error fetching comments:', error);
      });
  }, [threadId]);

  // Handle adding a new comment
  const handleAddComment = async (e) => {
    e.preventDefault();

    // Add the new comment to the top of the comments array
    const updatedComments = [newComment, ...comments];

    // Construct the updated content
    const updatedContent = {
      comments: updatedComments,
    };

    // Send a PUT request to update the thread document with the new comment
    try {
      const response = await fetch(`http://localhost:8080/threads/documents/${threadId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content: updatedContent }),
      });

      if (response.ok) {
        setComments(updatedComments); // Update the state with new comments
        setNewComment(''); // Clear the input field
        setResponseMessage('Comment added successfully!');
      } else {
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

