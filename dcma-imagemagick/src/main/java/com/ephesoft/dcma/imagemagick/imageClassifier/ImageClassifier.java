/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2012 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.dcma.imagemagick.imageclassifier;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Page.PageLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.imagemagick.IImageMagickCommonConstants;
import com.ephesoft.dcma.imagemagick.ImageMagicProperties;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.util.CustomMapClass;
import com.ephesoft.dcma.util.CustomValueSortedMap;

/**
 * This class contains the functionality to classify images based on the sample images kept in the sampleBaseFolderPath. The sample
 * images should be kept in the following fashion on the disk c:\<sample-folder-name>\<batch-class-id>\<document-name>\<page-name>. eg.
 * C:\ephesoft-data\Samples\1\India_Invoice\India_Invoice_First_Page. The thumbnails can be generated using the methods of the class
 * SampleThumbnailGenerator. The sampleBaseFolderPath is picked from the properties file imagemagick.properties.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.imagemagick.service.ImageProcessServiceImpl
 * 
 */
public class ImageClassifier {

	/**
	 * Constant LOGGER for proper logging in this class.
	 */
	protected final static Logger LOGGER = LoggerFactory.getLogger(ImageClassifier.class);

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * The utility which does the actual image comparison work.
	 */
	@Autowired
	// @Qualifier("imageComparisonUtil")
	private ImageComparisonUtil imageComparisonUtil;

	/**
	 * An instance of {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * @return the {@link PluginPropertiesService}
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @param pluginPropertiesService the {@link PluginPropertiesService} to set
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * This method takes in the batch InstanceID and subsequently gets the parsedXMLFile. Then it fetches all the Pages from it as they
	 * are with a document type unknown. and it classifies all the pages based on Image comparison. In the end the result is put in the
	 * batch.xml file.
	 * @param batchInstanceIdentifier {@link String}
	 * @param sBatchFolder {@link String}
	 * @throws IOException for any input output exception
	 * @throws DCMAApplicationException for any other type of exception that occurs.
	 */
	public void classifyAllImgsOfBatch(String batchInstanceIdentifier, String sBatchFolder) throws IOException,
			DCMAApplicationException {

		// Initialize properties
		LOGGER.info("Initializing properties...");
		Batch parsedXmlFile = batchSchemaService.getBatch(batchInstanceIdentifier);
		String batchClassIdentifier = parsedXmlFile.getBatchClassIdentifier();
		LOGGER.info("batchClassIdentifier = " + batchClassIdentifier);
		String sampleBaseFolderPath = batchSchemaService.getImageMagickBaseFolderPath(batchClassIdentifier, false);
		
		String imMetric = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN,
				ImageMagicProperties.CLASSIFY_IMAGES_COMP_METRIC);
		String imFuzz = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN,
				ImageMagicProperties.CLASSIFY_IMAGES_FUZZ_PERCNT);
		String maxVal = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN,
				ImageMagicProperties.CLASSIFY_IMAGES_MAX_RESULTS);

		LOGGER.info("Properties Initialized Successfully, sampleBaseFolderPath = " + sampleBaseFolderPath);

		// Get list of Page names from parsedXmlFile.
		List<String> listOfUnclasifiedPgPth = new LinkedList<String>();
		Map<String, Integer> unclassifiedPgIndexMap = new HashMap<String, Integer>();
		
		getPathOfPagesOfBatch(parsedXmlFile, listOfUnclasifiedPgPth, unclassifiedPgIndexMap, sBatchFolder);
		Map<String, CustomValueSortedMap> finalUnclasifiedPageConfidenceMap = performClassification(maxVal, imMetric, imFuzz,
				sampleBaseFolderPath, listOfUnclasifiedPgPth);
		
		List<Document> listOfDocuments = parsedXmlFile.getDocuments().getDocument();
		Document docuemnt = listOfDocuments.get(0);
		Pages pages = docuemnt.getPages();
		List<Page> listOfPages = pages.getPage();

		updateXmlObject(finalUnclasifiedPageConfidenceMap, unclassifiedPgIndexMap, listOfPages);
		batchSchemaService.updateBatch(parsedXmlFile);
	}

	private Map<String, CustomValueSortedMap> performClassification(String maxVal, String imMetric, String imFuzz, String sampleBaseFolderPath,
			List<String> listOfUnclasifiedPgPth) throws IOException, DCMAApplicationException {
		File batchClassFolder = new File(sampleBaseFolderPath);
		String[] documentTypeArray = batchClassFolder.list();

		int maxValue = ImageMagicKConstants.DEFAULT_MAX_VALUE_10;
		try {
			maxValue = Integer.valueOf(maxVal);
			LOGGER.info("maxValue = " + maxValue);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Max value is not a number. Using default value of 10. " + nfe.getMessage(), nfe);
		}
		if(documentTypeArray == null || documentTypeArray.length == 0) {
			throw new DCMAApplicationException(" Learning not done for batch class sample folder path:" + sampleBaseFolderPath);
		}
		// CustomValueSortedMap finalUnclasifiedPageConfidenceMap = new CustomValueSortedMap(maxValue);
		Map<String, CustomValueSortedMap> finalUnclasifiedPageConfidenceMap = new HashMap<String, CustomValueSortedMap>();
		for (String unclasifiedPagePath : listOfUnclasifiedPgPth) {
			CustomValueSortedMap sampleConfidenceList = new CustomValueSortedMap(maxValue);
			for (String documentName : documentTypeArray) {
				LOGGER.info("documentName = " + documentName);
				File fdocumentName = new File(documentName);
				if (fdocumentName.isHidden()) {
					continue;
				}
				List<String> listOfSamplePages = getListOfSamplePagesForDoc(documentName, sampleBaseFolderPath);
				for (String samplePageName : listOfSamplePages) {
					List<String> listOfSambleThumbsPaths = getListOfThumbnailPaths(samplePageName, documentName, sampleBaseFolderPath);
					double maxConfidence = getMaxCondifence(unclasifiedPagePath, listOfSambleThumbsPaths, imMetric, imFuzz);
					LOGGER.info("samplePageName = " + samplePageName + ", maxConfidence = " + maxConfidence);
					sampleConfidenceList.add(samplePageName, maxConfidence);
				}
			}
			finalUnclasifiedPageConfidenceMap.put(unclasifiedPagePath, sampleConfidenceList);
		}
		return finalUnclasifiedPageConfidenceMap;
	}

	/**
	 * This method takes in the batch InstanceID and subsequently gets the parsedXMLFile. Then it fetches all the Pages from it as they
	 * are with a document type unknown. and it classifies all the pages based on Image comparison. In the end the result is put in the
	 * batch.xml file.
	 * @param maxVal {@link String}
	 * @param imMetric {@link String}
	 * @param imFuzz {@link String}
	 * @param batchInstanceIdentifier {@link String}
	 * @param batchClassIdentifier {@link String}
	 * @param sBatchFolder {@link String}
	 * @param listOfPages {@link List<Page>}
	 * @throws IOException for any input output exception
	 * @throws DCMAApplicationException for any other type of exception that occurs.
	 */
	public void classifyAllImgsOfBatchInternal(String maxVal, String imMetric, String imFuzz,String batchInstanceIdentifier, String batchClassIdentifier, String sBatchFolder, List<Page> listOfPages) throws IOException,
			DCMAApplicationException {

		// Initialize properties
		LOGGER.info("Initializing properties...");
		LOGGER.info("batchClassIdentifier = " + batchClassIdentifier);
		String sampleBaseFolderPath = batchSchemaService.getImageMagickBaseFolderPath(batchClassIdentifier, false);
		LOGGER.info("Properties Initialized Successfully, sampleBaseFolderPath = " + sampleBaseFolderPath);

		// Get list of Page names from parsedXmlFile.
		List<String> listOfUnclasifiedPgPth = new LinkedList<String>();
		Map<String, Integer> unclassifiedPgIndexMap = new HashMap<String, Integer>();
		processPageList(listOfPages, listOfUnclasifiedPgPth, unclassifiedPgIndexMap, sBatchFolder);
		Map<String, CustomValueSortedMap> finalUnclasifiedPageConfidenceMap = performClassification(maxVal, imMetric, imFuzz, 
				sampleBaseFolderPath, listOfUnclasifiedPgPth);
		
		updateXmlObject(finalUnclasifiedPageConfidenceMap, unclassifiedPgIndexMap, listOfPages);
		
	}

	private void updateXmlObject(Map<String, CustomValueSortedMap> finalUnclasifiedPageConfidenceMap,
			Map<String, Integer> unclassifiedPgIndexMap, List<Page> listOfPages) {
		Set<String> unclasifiedPgsSet = finalUnclasifiedPageConfidenceMap.keySet();
		for (String unclassifiedPage : unclasifiedPgsSet) {
			CustomValueSortedMap listOfConfidenceValues = finalUnclasifiedPageConfidenceMap.get(unclassifiedPage);
			Integer indexOfPage = unclassifiedPgIndexMap.get(unclassifiedPage);
			if (indexOfPage == null) {
				continue;
			}
			Page unclassifiedPageNode = listOfPages.get(indexOfPage);
			PageLevelFields pageLevelFields = unclassifiedPageNode.getPageLevelFields();
			if (pageLevelFields == null) {
				pageLevelFields = new PageLevelFields();
				unclassifiedPageNode.setPageLevelFields(pageLevelFields);
			}
			List<DocField> listOfPageLevelFields = pageLevelFields.getPageLevelField();
			DocField pageLevelField = new DocField();
			listOfPageLevelFields.add(pageLevelField);
			
			if (listOfConfidenceValues.size() >= 1) {
				pageLevelField.setName(IImageMagickCommonConstants.IMAGE_COMPARE_CLASSIFICATION);
				pageLevelField.setValue(listOfConfidenceValues.last().getKey());
				pageLevelField.setConfidence(formatDecimalValue(listOfConfidenceValues.last().getValue()));
			} 
			AlternateValues alternativeValues = pageLevelField.getAlternateValues();
			if (alternativeValues == null) {
				alternativeValues = new AlternateValues();
				pageLevelField.setAlternateValues(alternativeValues);
			}
			List<Field> listOfAltValues = alternativeValues.getAlternateValue();
			Iterator<CustomMapClass> itr = listOfConfidenceValues.descendingIterator();
			while (itr.hasNext()) {
				CustomMapClass sampleConfidence = itr.next();
				Field alternateValue = new Field();
				alternateValue.setName(IImageMagickCommonConstants.IMAGE_COMPARE_CLASSIFICATION);
				alternateValue.setValue(sampleConfidence.getKey());
				alternateValue.setConfidence(formatDecimalValue(sampleConfidence.getValue()));
				listOfAltValues.add(alternateValue);

			}
		}
	}

	private double getMaxCondifence(final String unclasifiedPagePath, List<String> listOfSambleThumbsPaths,
			final String imMetric, final String imFuzz) throws IOException, DCMAApplicationException {
		if (imageComparisonUtil == null) {
			imageComparisonUtil = new ImageComparisonUtil(true);
		}
		double maxConfidence = 0;
		LOGGER.info("unclasifiedPagePath = " + unclasifiedPagePath);
		for (String sampleThumbnailPath : listOfSambleThumbsPaths) {
			LOGGER.info("sampleThumbnailPath = " + sampleThumbnailPath);
			double confidence = 0;
			try {
				File fSampleThumb = new File(sampleThumbnailPath);
				if (!fSampleThumb.exists()) {
					LOGGER.info("Thumbnail File does not exist filename = " + fSampleThumb);
					continue;
				}
				confidence = imageComparisonUtil.compareImagesRuntime(unclasifiedPagePath, sampleThumbnailPath, imMetric, imFuzz);
				LOGGER.info("confidence = " + confidence);
			} catch (Exception e) {
				LOGGER.info("Problem comparing images " + unclasifiedPagePath + "," + sampleThumbnailPath + " Exception = "
						+ e.getMessage(), e);
				continue;
			}
			if (confidence > maxConfidence) {
				maxConfidence = confidence;
			}
		}
		LOGGER.info("maxConfidence = " + maxConfidence);
		return maxConfidence;
	}

	private List<String> getListOfThumbnailPaths(final String samplePageType, final String documentType,
			final String sampleBaseFolderPath) {
		List<String> listOfThumbnailPaths = new LinkedList<String>();
		StringBuffer thumbnailFolderPath = new StringBuffer();
		thumbnailFolderPath.append(sampleBaseFolderPath);
		thumbnailFolderPath.append(File.separator);
		thumbnailFolderPath.append(documentType);
		thumbnailFolderPath.append(File.separator);
		thumbnailFolderPath.append(samplePageType);
		thumbnailFolderPath.append(File.separator);
		thumbnailFolderPath.append(IImageMagickCommonConstants.THUMBS);
		File thumbnailFolder = new File(thumbnailFolderPath.toString());
		if (thumbnailFolder.exists() && thumbnailFolder.isDirectory()) {
			String[] arrayOfThumbnailNames = thumbnailFolder.list();
			for (String nameOfSampleThumbnail : arrayOfThumbnailNames) {
				if (nameOfSampleThumbnail.endsWith(IImageMagickCommonConstants.SUFFIX_THUMBNAIL_SAMPLE_TIF)
						|| nameOfSampleThumbnail.endsWith(IImageMagickCommonConstants.SUFFIX_THUMBNAIL_SAMPLE_PNG)) {
					listOfThumbnailPaths.add(thumbnailFolderPath.toString() + File.separator + nameOfSampleThumbnail);
				}
			}

		} else {
			throw new DCMABusinessException("Thumbnail Folder Does not exist foldername=" + thumbnailFolder);
		}
		return listOfThumbnailPaths;
	}

	private List<String> getListOfSamplePagesForDoc(String documentType, String sampleBaseFolderPath) {

		List<String> listOfSamplePageTypes = new LinkedList<String>();
		StringBuffer sampleDocumentPath = new StringBuffer();
		sampleDocumentPath.append(sampleBaseFolderPath);
		sampleDocumentPath.append(File.separator);
		sampleDocumentPath.append(documentType);

		File fsampleDocumentPath = new File(sampleDocumentPath.toString());
		if (fsampleDocumentPath.exists() && fsampleDocumentPath.isDirectory()) {
			String[] arrOfSamplePageTypes;
			arrOfSamplePageTypes = fsampleDocumentPath.list();
			for (String samplePagetypeName : arrOfSamplePageTypes) {
				if (samplePagetypeName.equals(IImageMagickCommonConstants.THUMBS)) {
					continue;
				}
				File fSamplePagetypeName = new File(samplePagetypeName);
				if (fSamplePagetypeName.isHidden()) {
					continue;
				}
				listOfSamplePageTypes.add(samplePagetypeName);
			}

		} else {
			throw new DCMABusinessException("Could not find the sample document folder=" + fsampleDocumentPath);
		}
		return listOfSamplePageTypes;
	}

	private void getPathOfPagesOfBatch(Batch parsedXmlFile, List<String> listOfUnclasifiedPages,
			Map<String, Integer> unclassifiedPgIndexMap, String sBatchFolder) {
		Documents documents = parsedXmlFile.getDocuments();
		boolean valid = true;
		if (documents == null) {
			valid = false;
		}
		List<Document> listOfDocuments = null;
		List<Page> listOfPages = null;
		if (valid) {
			listOfDocuments = documents.getDocument();
			if (listOfDocuments == null || listOfDocuments.isEmpty()) {
				valid = false;
			} else {
				Document unknownDocument = listOfDocuments.get(0);
				Pages pages = unknownDocument.getPages();
				listOfPages = pages.getPage();
				if (listOfPages == null || listOfPages.isEmpty()) {
					valid = false;
				}
			}
		}
		if (valid) {
			processPageList(listOfPages, listOfUnclasifiedPages, unclassifiedPgIndexMap, sBatchFolder);
		}
	}

	private void processPageList(List<Page> listOfPages, List<String> listOfUnclasifiedPages,
			Map<String, Integer> unclassifiedPgIndexMap, String sBatchFolder) {
		StringBuffer unclassifiedPgPath;
		int index = 0;
		for (Page page : listOfPages) {
			unclassifiedPgPath = new StringBuffer();
			unclassifiedPgPath.append(sBatchFolder);
			unclassifiedPgPath.append(File.separator);
			unclassifiedPgPath.append(page.getComparisonThumbnailFileName());
			listOfUnclasifiedPages.add(unclassifiedPgPath.toString());
			unclassifiedPgIndexMap.put(unclassifiedPgPath.toString(), Integer.valueOf(index));
			index++;

		}
	}

	private float formatDecimalValue(double decimalValue) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Float.valueOf(twoDForm.format(decimalValue));

	}
}
