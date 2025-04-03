import { Client, Account, ID, Databases, Storage, Query } from "appwrite";
import conf from "../conf/conf.js";

export class Service {
    client = new Client();
    databases;
    bucket;

    constructor() {
        this.client = new Client()
            .setEndpoint(conf.appwriteUrl) // Set API URL
            .setProject(conf.appwriteProjectId); // Set Project ID

        this.databases = new Databases(this.client);
        this.bucket = new Storage(this.client);
    }

    async createPost({ title, slug, content, featuredImage, status, userId }) {
        try {
            return await this.databases.createDocument(
                conf.appwriteDatabaseId,
                conf.appwriteCollectionId,
                slug,
                {
                    title,
                    content,
                    featuredImage,
                    status,
                    userId,
                }
            );
        } catch (err) {
            console.log("Appwrite Error: ", err);
        }
    }

    async updatePost({ title, slug, content, featuredImage, status }) {
        try {
            return await this.databases.updateDocument(
                conf.appwriteDatabaseId,
                conf.appwriteCollectionId,
                slug,
                {
                    title,
                    content,
                    featuredImage,
                    status,
                }
            );
        } catch (err) {
            console.log("Appwrite Error: ", err);
        }
    }

    async deletePost(slug) {
        try {
            await this.databases.deleteDocument(
                conf.appwriteDatabaseId,
                conf.appwriteCollectionId,
                slug
            );
            return true;
        } catch (err) {
            console.log("Appwrite Error: ", err);
            return false;
        }
    }

    async getPost(slug) {
        try {
            return await this.databases.getDocument(
                conf.appwriteBucketId,
                conf.appwriteCollectionId,
                slug
            );
        } catch (err) {
            console.log("Appwrite Error: ", err);
        }
    }

    async getAllPosts(queries = [Query.equal("status", "active")]) {
        try {
            await this.databases.listDocuments(
                conf.appwriteDatabaseId,
                conf.appwriteCollectionId,
                queries
            );
        } catch (err) {
            console.log("Appwrite Error: ", err);
            return false;
        }
    }

    //file upload
    async uploadFile(file) {
        try {
            return await this.bucket.createFile(
                conf.appwriteBucketId,
                ID.unique(),
                file
            );
        } catch (err) {
            console.log("Appwrite Error: ", err);
            return false;
        }
    }

    async deleteFile(fileId) {
        try {
            await this.bucket.deleteFile(conf.appwriteBucketId, fileId);
            return true;
        } catch (err) {
            console.log("Appwrite Error: ", err);
            return false;
        }
    }

    getfileUrlPreview(fileId) {
        return this.bucket.getFilePreview(conf.appwriteBucketId, fileId);
    }
}

const service = new Service();
export default service;
