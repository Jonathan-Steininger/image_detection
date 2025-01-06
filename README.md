# Image Detection Service
Ingests user images, analyzes them for object detection, and returns the enhanced content. It should implement the following specification

### API Specification

GET /images
- Returns HTTP 200 OK with a JSON response containing all image metadata.

GET /images?objects=dog,cat
- Returns an HTTP OK with a JSON response body containing only images that have the detected objects specified in the query parameter

GET /images/{imageId}
- Returns an HTTP 200 OK with a JSON response containing image metadata for the specified image.

GET /images/view/{imageId}
- Returns an HTTP 200 OK with the actual visual image for the specified imageId.

POST /images
- Send a JSON request body including an image file or URL, an optional label for the image, and an optional field to enable object detection
- Returns an HTTP 200 OK with a JSON response body including the image data, its label (generate one if the user did not provide it), its identifier provided by the persistent data store, and any objects detected (if object detection was enabled). 