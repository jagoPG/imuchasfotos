/*
 * Jagoba Pérez Copyright 2014
 * This program is distributed under the terms of the GNU General Public License
 * 
 * ImageManagerFilter.java
 * 
 * This file is part of Image Manager.
 * 
 * Image Manager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Image Manager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Image Manager.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jagobapg.imuchasfotos.gui.imanager;

import com.jagobapg.imuchasfotos.dto.Author;
import com.jagobapg.imuchasfotos.dto.Folder;
import com.jagobapg.imuchasfotos.dto.Key;
import com.jagobapg.imuchasfotos.dto.Photo;
import com.jagobapg.imuchasfotos.dto.Topic;
import com.jagobapg.imuchasfotos.sqlite.DBQueries;

/**
 * This class has the methods for filtering images through a certain criteria.
 */
public final class ImageManagerFilter {

    private GUIImageManager parent;
    protected Object filter_aux;
    protected Object filter_aux2;
    protected Object and_or_aux;

    public enum EFilter {
        NO_FILTER, FOLDER_FILTER, YEAR_FILTER, NAME_FILTER, TOPIC_FILTER, KEY_FILTER, AUTHOR_FILTER, MIXED_FILTER, FROM_ID_QUERY, TO_ID_QUERY, COUNT_PHOTO_QUERY, OR, AND
    }

    /**
     * Creates an ImageManagerFilter, it will decide what photos have to be
     * loaded in the arrays, for its later use in the JList.
     *
     * @param parent
     */
    public ImageManagerFilter(GUIImageManager parent) {
        this.parent = parent;
    }

    /**
     * Depending on the parent.imageFilter variable a filter will be used. *
     */
    public void applyFilter() {
        switch (parent.imageFilter) {
            case NO_FILTER: // Load all photos.
                initNoneFilter();
                break;
            case FOLDER_FILTER: // Load photos of the desired folder.
                initFolderFilter();
                break;
            case NAME_FILTER:
                initNameFilter();
                break;
            case KEY_FILTER:
                initKeyFilter();
                break;
            case AUTHOR_FILTER:
                initAuthorFilter();
                break;
            case TOPIC_FILTER:
                initTopicFilter();
                break;
            case YEAR_FILTER:
                initYearFilter();
                break;
            case MIXED_FILTER:
                initMixedFilter();
                break;
            default:
        }
    }

    /**
     * Depending on the parent.imageFilter variable a filter will be used for
     * loading the previous page. 
	 *
     */
    public void previousPageFilter() {
        Photo first = (Photo) parent.aPhotoCurrent[0];
        switch (parent.imageFilter) { //Depending on the filter state load a photo set.
            case NO_FILTER:
                if (parent.currentPage != 1) {
                    parent.aPhotoBefore = DBQueries.INSTANCE.getPhotosBeforeID(GUIImageManager.PHOTOS_EACH_PAGE, first.getId());
                }
                break;
            case FOLDER_FILTER:
                if (parent.currentPage != 1) {
                    parent.aPhotoBefore = DBQueries.INSTANCE.getPhotosBeforeID_folderFilter(GUIImageManager.PHOTOS_EACH_PAGE, first.getId(), first.getIdDir());
                }
                break;
            case NAME_FILTER:
                String name = (String) this.filter_aux;
                if (parent.currentPage != 1) {
                    parent.aPhotoBefore = DBQueries.INSTANCE.getPhotosBeforeID_nameFilter(GUIImageManager.PHOTOS_EACH_PAGE, first.getId(), name);
                }
                break;
            case KEY_FILTER:
                Key key = (Key) this.filter_aux;
                if (parent.currentPage != 1) {
                    parent.aPhotoBefore = DBQueries.INSTANCE.getPhotosBeforeID_keyFilter(GUIImageManager.PHOTOS_EACH_PAGE, first.getId(), key.getId());
                }
            case AUTHOR_FILTER:
                Author author = (Author) this.filter_aux;
                if (parent.currentPage != 1) {
                    parent.aPhotoBefore = DBQueries.INSTANCE.getPhotosBeforeID_authorFilter(GUIImageManager.PHOTOS_EACH_PAGE, first.getId(), author.getId());
                }
                break;
            case TOPIC_FILTER:
                Topic topic = (Topic) this.filter_aux;
                if (parent.currentPage != 1) {
                    parent.aPhotoBefore = DBQueries.INSTANCE.getPhotosBeforeID_topicFilter(GUIImageManager.PHOTOS_EACH_PAGE, first.getId(), topic.getId());
                }
                break;
            case YEAR_FILTER:
                int year = Integer.parseInt((String) this.filter_aux);
                if (parent.currentPage != 1) {
                    parent.aPhotoBefore = DBQueries.INSTANCE.getPhotosBeforeID_yearFilter(GUIImageManager.PHOTOS_EACH_PAGE, first.getId(), year);
                }
                break;
            case MIXED_FILTER:
                if (parent.currentPage != 1) {
                    parent.aPhotoBefore = DBQueries.INSTANCE.getPhotosBeforeID_mixedFilter(GUIImageManager.PHOTOS_EACH_PAGE, first.getId(), this.filter_aux, (EFilter) this.and_or_aux, this.filter_aux2);
                }
                break;
            default:
        }
    }

    /**
     * Depending on the parent.imageFilter variable a filter will be used for
     * loading the next page. 
	 *
     */
    public void nextPageFilter() {
        Photo last = (Photo) parent.aPhotoCurrent[GUIImageManager.PHOTOS_EACH_PAGE - 1];

        switch (parent.imageFilter) { //Depending on the filter state load a photo set.
            case NO_FILTER:
                if (parent.currentPage != parent.maxPages) { //Last page.
                    parent.aPhotoAfter = DBQueries.INSTANCE.getPhotosFromID(GUIImageManager.PHOTOS_EACH_PAGE, last.getId());
                }
                break;
            case FOLDER_FILTER:
                if (parent.currentPage != parent.maxPages) { //Last page.
                    parent.aPhotoAfter = DBQueries.INSTANCE.getPhotosFromID_folderFilter(GUIImageManager.PHOTOS_EACH_PAGE, last.getId(), last.getIdDir());
                }
                break;
            case NAME_FILTER:
                if (parent.currentPage != parent.maxPages) { //Last page.
                    String name = (String) this.filter_aux;
                    parent.aPhotoAfter = DBQueries.INSTANCE.getPhotosFromID_nameFilter(GUIImageManager.PHOTOS_EACH_PAGE, last.getId(), name);
                }
                break;
            case KEY_FILTER:
                if (parent.currentPage != parent.maxPages) { //Last page.
                    Key key = (Key) this.filter_aux;
                    parent.aPhotoAfter = DBQueries.INSTANCE.getPhotosFromID_keyFilter(GUIImageManager.PHOTOS_EACH_PAGE, last.getId(), key.getId());
                }
                break;
            case AUTHOR_FILTER:
                if (parent.currentPage != parent.maxPages) { //Last page.
                    Author author = (Author) this.filter_aux;
                    parent.aPhotoAfter = DBQueries.INSTANCE.getPhotosFromID_authorFilter(GUIImageManager.PHOTOS_EACH_PAGE, last.getId(), author.getId());
                }
                break;
            case TOPIC_FILTER:
                if (parent.currentPage != parent.maxPages) { //Last page.
                    Topic topic = (Topic) this.filter_aux;
                    parent.aPhotoAfter = DBQueries.INSTANCE.getPhotosFromID_topicFilter(GUIImageManager.PHOTOS_EACH_PAGE, last.getId(), topic.getId());
                }
                break;
            case YEAR_FILTER:
                if (parent.currentPage != parent.maxPages) { //Last page.
                    int year = Integer.parseInt((String) this.filter_aux);
                    parent.aPhotoAfter = DBQueries.INSTANCE.getPhotosFromID_yearFilter(GUIImageManager.PHOTOS_EACH_PAGE, last.getId(), year);
                }
                break;
            case MIXED_FILTER:
                if (parent.currentPage != parent.maxPages) { //Last page.
                    parent.aPhotoBefore = DBQueries.INSTANCE.getPhotosFromID_mixedFilter(GUIImageManager.PHOTOS_EACH_PAGE, last.getId(), this.filter_aux, (EFilter) this.and_or_aux, this.filter_aux2);
                }
                break;
            default:
        }
    }

    //None filter.
    /**
     * Load photo information when no filter is applied. *
     */
    private void initNoneFilter() {
        int numImages;

        // Clear elements
        parent.loadInitialPhotosPage();

        parent.currentPage = 1;
        parent.maxPages = DBQueries.INSTANCE.getNumberFoldersPhoto(GUIImageManager.PHOTOS_EACH_PAGE);

        numImages = DBQueries.INSTANCE.getSizeAllPhotos();
        parent.lblNumImages.setText("Imágenes: " + numImages);

        // Photos of current list
        parent.aPhotoCurrent = DBQueries.INSTANCE.getPhotosFromID(GUIImageManager.PHOTOS_EACH_PAGE, 0);
        for (Photo p : parent.aPhotoCurrent) {
            parent.dlmPhoto.addElement(p);
        }

        // Photos of next list
        if (parent.currentPage + 1 <= parent.maxPages) {
            Photo last = (Photo) parent.dlmPhoto.get(GUIImageManager.PHOTOS_EACH_PAGE - 1);
            parent.aPhotoAfter = DBQueries.INSTANCE.getPhotosFromID(GUIImageManager.PHOTOS_EACH_PAGE, last.getId());
        }
    }
    //End of none filter.

    //Folder filter.
    /**
     * Load photo information when folder filter is applied. *
     */
    private void initFolderFilter() {
        Folder f;
        int numImages;

        // Clear elements
        parent.loadInitialPhotosPage();

        f = (Folder) parent.cbxFolders.getSelectedItem();
        parent.maxPages = DBQueries.INSTANCE.getNumberFolders_folderFilter(GUIImageManager.PHOTOS_EACH_PAGE, f.getId());

        numImages = DBQueries.INSTANCE.getSize_folderFilter(f.getId());
        parent.lblNumImages.setText("Imágenes: " + numImages);

        // Photos of current list
        parent.aPhotoCurrent = DBQueries.INSTANCE.getPhotosFromID_folderFilter(GUIImageManager.PHOTOS_EACH_PAGE, 0, f.getId());
        for (Photo p : parent.aPhotoCurrent) {
            parent.dlmPhoto.addElement(p);
        }

        /* Photos of next list. */
        if (parent.currentPage + 1 <= parent.maxPages) {
            Photo last = (Photo) parent.dlmPhoto.get(GUIImageManager.PHOTOS_EACH_PAGE - 1);
            parent.aPhotoAfter = DBQueries.INSTANCE.getPhotosFromID_folderFilter(GUIImageManager.PHOTOS_EACH_PAGE, last.getId(), last.getIdDir());
        }
    }
    //End of folder filter.

    //Name filter.
    /**
     * Load photo information when name filter is applied. *
     */
    protected void initNameFilter() {
        String name;
        int numImages;

        name = (String) this.filter_aux;

        parent.loadInitialPhotosPage();
        parent.maxPages = DBQueries.INSTANCE.getNumberFolders_nameFilter(GUIImageManager.PHOTOS_EACH_PAGE, name);

        numImages = DBQueries.INSTANCE.getSize_nameFilter(name);
        parent.lblNumImages.setText("Imágenes: " + numImages);

        /* Photos of current list. */
        parent.aPhotoCurrent = DBQueries.INSTANCE.getPhotosFromID_nameFilter(GUIImageManager.PHOTOS_EACH_PAGE, 0, name);
        for (Photo p : parent.aPhotoCurrent) {
            parent.dlmPhoto.addElement(p);
        }

        /* Photos of next list. */
        if (parent.currentPage + 1 <= parent.maxPages) {
            Photo last = (Photo) parent.dlmPhoto.get(GUIImageManager.PHOTOS_EACH_PAGE - 1);
            parent.aPhotoAfter = DBQueries.INSTANCE.getPhotosFromID_nameFilter(GUIImageManager.PHOTOS_EACH_PAGE, last.getId(), name);
        }
    }
    //End of name filter.

    //Key filter.
    /**
     * Load photo information when key filter is applied. *
     */
    protected void initKeyFilter() {
        Key key;
        int numImages;

        key = (Key) this.filter_aux;

        parent.loadInitialPhotosPage();
        parent.maxPages = DBQueries.INSTANCE.getNumberFolders_keyFilter(GUIImageManager.PHOTOS_EACH_PAGE, key.getId());

        numImages = DBQueries.INSTANCE.getSize_keyFilter(key.getId());
        parent.lblNumImages.setText("Imágenes: " + numImages);

        /* Photos of current list. */
        parent.aPhotoCurrent = DBQueries.INSTANCE.getPhotosFromID_keyFilter(GUIImageManager.PHOTOS_EACH_PAGE, 0, key.getId());
        for (Photo p : parent.aPhotoCurrent) {
            parent.dlmPhoto.addElement(p);
        }

        /* Photos of next list. */
        if (parent.currentPage + 1 <= parent.maxPages) {
            Photo last = (Photo) parent.dlmPhoto.get(GUIImageManager.PHOTOS_EACH_PAGE - 1);
            parent.aPhotoAfter = DBQueries.INSTANCE.getPhotosFromID_keyFilter(GUIImageManager.PHOTOS_EACH_PAGE, last.getId(), key.getId());
        }
    }
    //End of key filter.

    //Author filter.
    /**
     * Load photo information when author filter is applied. *
     */
    protected void initAuthorFilter() {
        Author author;
        int numImages;

        author = (Author) this.filter_aux;

        parent.loadInitialPhotosPage();
        parent.maxPages = DBQueries.INSTANCE.getNumberFolders_authorFilter(GUIImageManager.PHOTOS_EACH_PAGE, author.getId());

        numImages = DBQueries.INSTANCE.getSize_authorFilter(author.getId());
        parent.lblNumImages.setText("Imágenes: " + numImages);

        // Photos of current list
        parent.aPhotoCurrent = DBQueries.INSTANCE.getPhotosFromID_authorFilter(GUIImageManager.PHOTOS_EACH_PAGE, 0, author.getId());
        for (Photo p : parent.aPhotoCurrent) {
            parent.dlmPhoto.addElement(p);
        }

        // Photos of next list
        if (parent.currentPage + 1 <= parent.maxPages) {
            Photo last = (Photo) parent.dlmPhoto.get(GUIImageManager.PHOTOS_EACH_PAGE - 1);
            parent.aPhotoAfter = DBQueries.INSTANCE.getPhotosFromID_authorFilter(GUIImageManager.PHOTOS_EACH_PAGE, last.getId(), author.getId());
        }
    }
    //End of author filter.

    //Topic filter.
    /**
     * Load photo information when topic filter is applied. *
     */
    protected void initTopicFilter() {
        Topic topic;
        int numImages;

        topic = (Topic) this.filter_aux;

        parent.loadInitialPhotosPage();
        parent.maxPages = DBQueries.INSTANCE.getNumberFolders_topicFilter(GUIImageManager.PHOTOS_EACH_PAGE, topic.getId());

        numImages = DBQueries.INSTANCE.getSize_topicFilter(topic.getId());
        parent.lblNumImages.setText("Imágenes: " + numImages);

        // Photos of current list
        parent.aPhotoCurrent = DBQueries.INSTANCE.getPhotosFromID_topicFilter(GUIImageManager.PHOTOS_EACH_PAGE, 0, topic.getId());
        for (Photo p : parent.aPhotoCurrent) {
            parent.dlmPhoto.addElement(p);
        }

        // Photos of next list
        if (parent.currentPage + 1 <= parent.maxPages) {
            Photo last = (Photo) parent.dlmPhoto.get(GUIImageManager.PHOTOS_EACH_PAGE - 1);
            parent.aPhotoAfter = DBQueries.INSTANCE.getPhotosFromID_topicFilter(GUIImageManager.PHOTOS_EACH_PAGE, last.getId(), topic.getId());
        }
    }
    //End of topic filter.

    //Year filter.
    /**
     * Load photo information when year filter is applied. *
     */
    protected void initYearFilter() {
        int year, numImages;

        year = Integer.parseInt((String) filter_aux);

        parent.loadInitialPhotosPage();
        parent.maxPages = DBQueries.INSTANCE.getNumberFolders_yearFilter(GUIImageManager.PHOTOS_EACH_PAGE, year);

        numImages = DBQueries.INSTANCE.getSize_yearFilter(year);
        parent.lblNumImages.setText("Imágenes: " + numImages);

        /* Photos of current list. */
        parent.aPhotoCurrent = DBQueries.INSTANCE.getPhotosFromID_yearFilter(GUIImageManager.PHOTOS_EACH_PAGE, 0, year);
        for (Photo p : parent.aPhotoCurrent) {
            parent.dlmPhoto.addElement(p);
        }

        /* Photos of next list. */
        if (parent.currentPage + 1 <= parent.maxPages) {
            Photo last = (Photo) parent.dlmPhoto
                    .get(GUIImageManager.PHOTOS_EACH_PAGE - 1);
            parent.aPhotoAfter = DBQueries.INSTANCE.getPhotosFromID_yearFilter(GUIImageManager.PHOTOS_EACH_PAGE, last.getId(), year);
        }
    }
    //End of year filter.

    //Mixed filter.
    /**
     * Load photo information when mixed filter is applied. *
     */
    protected void initMixedFilter() {
        int numImages;

        parent.loadInitialPhotosPage();
        parent.maxPages = DBQueries.INSTANCE.getNumberFolders_mixedFilter(GUIImageManager.PHOTOS_EACH_PAGE, this.filter_aux, (EFilter) this.and_or_aux, this.filter_aux2);

        numImages = DBQueries.INSTANCE.getSize_mixedFilter(this.filter_aux, (EFilter) this.and_or_aux, this.filter_aux2);
        parent.lblNumImages.setText("Imágenes: " + numImages);

        /* Photos of current list. */
        parent.aPhotoCurrent = DBQueries.INSTANCE.getPhotosFromID_mixedFilter(GUIImageManager.PHOTOS_EACH_PAGE, 0, this.filter_aux, (EFilter) this.and_or_aux, this.filter_aux2);
        for (Photo p : parent.aPhotoCurrent) {
            parent.dlmPhoto.addElement(p);
        }

        // photos of next list
        if (parent.currentPage + 1 <= parent.maxPages) {
            Photo last = (Photo) parent.dlmPhoto.get(GUIImageManager.PHOTOS_EACH_PAGE - 1);
            parent.aPhotoAfter = DBQueries.INSTANCE.getPhotosFromID_mixedFilter(GUIImageManager.PHOTOS_EACH_PAGE, last.getId(), this.filter_aux, (EFilter) this.and_or_aux, this.filter_aux2);
        }
    }
    // End of year filter.

}
