package br.com.mrcontador.service.dto;

import java.io.InputStream;

public class FileDTO {
	
	private byte[] bytes;
	private String contentType;
	private InputStream inputStream;
	private String name;
	private String originalFilename;
	private Long size;
	private String url;
	private String eTag;
	private String bucket;
	private String s3Url;
	private String s3Dir;
	
	
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOriginalFilename() {
		return originalFilename;
	}
	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String geteTag() {
		return eTag;
	}
	public void seteTag(String eTag) {
		this.eTag = eTag;
	}
	public String getBucket() {
		return bucket;
	}
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	public String getS3Url() {
		return s3Url;
	}
	public void setS3Url(String s3Url) {
		this.s3Url = s3Url;
	}
	
	public String getS3Dir() {
		return s3Dir;
	}
	public void setS3Dir(String s3Dir) {
		this.s3Dir = s3Dir;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bucket == null) ? 0 : bucket.hashCode());
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((eTag == null) ? 0 : eTag.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((originalFilename == null) ? 0 : originalFilename.hashCode());
		result = prime * result + ((s3Url == null) ? 0 : s3Url.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileDTO other = (FileDTO) obj;
		if (bucket == null) {
			if (other.bucket != null)
				return false;
		} else if (!bucket.equals(other.bucket))
			return false;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		if (eTag == null) {
			if (other.eTag != null)
				return false;
		} else if (!eTag.equals(other.eTag))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (originalFilename == null) {
			if (other.originalFilename != null)
				return false;
		} else if (!originalFilename.equals(other.originalFilename))
			return false;
		if (s3Url == null) {
			if (other.s3Url != null)
				return false;
		} else if (!s3Url.equals(other.s3Url))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FileDTO [contentType=" + contentType + ", name=" + name + ", originalFilename=" + originalFilename
				+ ", size=" + size + ", url=" + url + ", eTag=" + eTag + ", bucket=" + bucket + ", s3Url=" + s3Url
				+ "]";
	}
	
	

}
