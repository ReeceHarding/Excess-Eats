import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp();

interface NotificationData {
    title: string;
    description: string;
    postId: string;
    radius: number;
}

export const sendNewFoodPostNotification = functions.https.onCall(
    async (data: NotificationData, context: functions.https.CallableContext) => {
        console.log('Received notification request:', data);
        
        try {
            // Ensure user is authenticated
            if (!context.auth) {
                throw new functions.https.HttpsError(
                    'unauthenticated',
                    'User must be authenticated'
                );
            }

            const { title, description, postId } = data;

            // Create the notification message
            const message: admin.messaging.Message = {
                notification: {
                    title: 'New Food Available Nearby',
                    body: `${title} - ${description}`
                },
                data: {
                    postId: postId,
                    title: title,
                    message: description
                },
                topic: 'new_food_posts'
            };

            // Send the notification
            console.log('Sending message:', message);
            const result = await admin.messaging().send(message);
            console.log('Successfully sent message:', result);
            
            return { success: true };
        } catch (error) {
            console.error('Error sending notification:', error);
            throw new functions.https.HttpsError(
                'internal',
                'Error sending notification'
            );
        }
    }
); 