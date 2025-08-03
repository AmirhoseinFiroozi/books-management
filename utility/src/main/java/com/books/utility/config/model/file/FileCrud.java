package com.books.utility.config.model.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author imax on 8/3/19
 */

@Getter
@Setter
@NoArgsConstructor
public class FileCrud implements Diffable<FileCrud> {
    private String baseFilePath;
    private String tempFilePath;
    private String tempFileUrl;
    private float imageQuality;
    private List<String> allowedExtensions = new ArrayList<>();

    public FileCrud(FileCrud fileCrud) {
        this.baseFilePath = fileCrud.getBaseFilePath();
        this.tempFilePath = fileCrud.getTempFilePath();
        this.tempFileUrl = fileCrud.getTempFileUrl();
        this.imageQuality = fileCrud.getImageQuality();
        this.allowedExtensions = fileCrud.getAllowedExtensions();
    }

    @Override
    public DiffResult<FileCrud> diff(FileCrud fileCrud) {
        return new DiffBuilder(this, fileCrud, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("baseFilePath", this.baseFilePath, fileCrud.getBaseFilePath())
                .append("tempFilePath", this.tempFilePath, fileCrud.getTempFilePath())
                .append("tempFileUrl", this.tempFileUrl, fileCrud.getTempFileUrl())
                .append("imageQuality", this.imageQuality, fileCrud.getImageQuality())
                .append("allowedExtensions", this.allowedExtensions, fileCrud.getAllowedExtensions())
                .build();
    }
}
